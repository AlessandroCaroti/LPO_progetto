(* FUNCTION TYPES

(* auxiliary functions *)
 
name : ident -> string 

toString : typ -> string 

error_message : string -> string -> string 

emit_error : typ -> typ -> 'a 

check_type : typ -> typ -> typ

conversion_error : string -> 'a

get_elem_type_list : typ -> typ

print : value -> unit

println : value -> unit

to_int : value -> int

to_list : value -> value list

(* functions on environments *)

empty_level : ident -> 'a

no_levels_error : unit -> 'a

initial_env : (ident -> 'a) list = [<fun>]

enter_level : (ident -> 'a) list -> (ident -> 'a) list

exit_level : 'a list -> 'a list

look_up : ident -> (ident -> 'a) list -> 'a

update_level : ('a -> 'b) -> 'b -> 'a -> 'a -> 'b

dec : ('a -> 'b) list -> 'b -> 'a -> ('a -> 'b) list

defined : 'a -> ('a -> 'b) -> bool

update : (ident -> 'a) list -> 'a -> ident -> (ident -> 'a) list

(* functions defining the static semantics *)

wfExp : (ident -> typ) list -> exp -> typ

wfExpSeq : (ident -> typ) list -> exp_seq -> typ

wfStmt : (ident -> typ) list -> stmt -> (ident -> typ) list

wfStmtSeq : (ident -> typ) list -> stmt_seq -> (ident -> typ) list

wfProg : prog -> unit

(* functions defining the dynamic semantics *)

evExp : (ident -> value) list -> exp -> value

evExpSeq : (ident -> value) list -> exp_seq -> value

evStmt : (ident -> value) list -> stmt -> (ident -> value) list

evStmtSeq : (ident -> value) list -> stmt_seq -> (ident -> value) list 

evProg : prog -> unit

*)

type ident = Id of string;;

let name = function Id name -> name;;

type 

  exp = Add of exp*exp | Mul of exp*exp | Prefix of exp*exp | Sign of exp | List of exp_seq | Num of int | Var of ident 

and

  exp_seq = SingleExp of exp | MoreExp of exp * exp_seq;;

type 

  stmt = Assign of ident*exp | VarStmt of ident*exp | Print of exp | For of ident * exp * stmt_seq

and

  stmt_seq = SingleStmt of stmt | MoreStmt of stmt * stmt_seq;;

type prog = Prog of stmt_seq;;

type typ = Int | List of typ;;

(* error handling and reporting *)

(* static errors *)

exception TypeError of string;;

let rec toString=function  (* string conversion for types *)
    Int -> "int"
  | List ty -> toString(ty)^" list";;

let error_message expected found = "Found type "^found^" expected "^expected;;

let emit_error expected found = raise (TypeError(error_message (toString expected) (toString found)));; 

let check_type expected found = if found=expected then found else emit_error expected found;;

(* dynamic errors *)

exception EvalError of string;;

let conversion_error ty=raise (EvalError("Could not convert value to "^ty));; 

(* environments, both static and dynamic *)

let empty_level = fun id -> failwith ("Undefined variable "^name(id));; 

let no_levels_error () = failwith "Fatal error: no more scope levels";;

let initial_env = [empty_level];; (* just the empty top-level scope *)

let enter_level env = empty_level::env;; (* enters a new nested scope *)

let exit_level = function  (* exits the current nested scope *)
    _::env -> env
  | [] -> no_levels_error();;

(* variable look up *)

let rec look_up id = function 
    [] ->  empty_level id (* forces exception *)
  | nested_level::env -> try nested_level id with _ -> look_up id env;;

(* variable declaration *)

let update_level scope_level payload new_id = fun id -> if id=new_id then payload else scope_level id;; 

let dec env payload id = match env with
    nested_level::other_levels -> update_level nested_level payload id::other_levels
  | [] -> no_levels_error();;

(* variable update *)

let defined id scope_level = try let _=scope_level id in true with _ -> false;;

let rec update env payload updated_id = match env with
    nested_level::other_levels ->
    if defined updated_id nested_level
    then update_level nested_level payload updated_id::other_levels
    else nested_level::update other_levels payload updated_id 
  | [] -> let _=empty_level updated_id in [];; (* forces exception *)

(* static semantics *)

let get_elem_type_list = function (* gets the elem type of a list type *)
    List ty -> ty
  | ty -> raise (TypeError(error_message "list" (toString ty)));;

let rec wfExp env=function 
    Add(exp1,exp2) | Mul(exp1,exp2) -> let _=check_type Int (wfExp env exp1) in  check_type Int (wfExp env exp2)
  | Prefix(exp1,exp2) -> let ty = wfExp env exp1 in check_type (List ty) (wfExp env exp2)
  | Sign exp -> check_type Int (wfExp env exp)
  | List exp_seq -> List (wfExpSeq env exp_seq)
  | Num _ -> Int
  | Var id -> look_up id env

and 

  wfExpSeq env=function 
    SingleExp exp -> wfExp env exp
  | MoreExp(exp,exp_seq) -> check_type (wfExp env exp) (wfExpSeq env exp_seq);;

let rec wfStmt env=function
    Assign(id,exp) -> let _=check_type (look_up id env) (wfExp env exp) in env
  | VarStmt(id,exp) -> dec env (wfExp env exp) id
  | Print exp -> let _=wfExp env exp in env
  | For(id,exp,stmt_seq) ->  let ty=get_elem_type_list (wfExp env exp) in let env2 = dec (enter_level env) ty id in let _=wfStmtSeq env2 stmt_seq in exit_level env2 

and 

  wfStmtSeq env=function 
    SingleStmt stmt -> wfStmt env stmt
  | MoreStmt(stmt,stmt_seq) -> wfStmtSeq (wfStmt env stmt) stmt_seq;;

let wfProg = function Prog stmt_seq -> try let _=wfStmtSeq initial_env stmt_seq in () with Failure msg -> raise (TypeError msg) | exc -> raise exc ;;

(* dynamic semantics *)

type value = Int of int | List of value list;;

let rec print=function 
  Int i -> print_int i |
  List l -> let size=List.length l in print_string "[";List.iteri (fun i x->print x; print_string (if i+1<size then ", " else "")) l;print_string "]";;

let println v = print v; print_newline();;

(* conversion functions *)

let to_int = function
    Int i -> i |
    _ -> conversion_error "int";;

let to_list = function
    List l -> l |
    _ -> conversion_error "list";;

let rec evExp env=function 
  | Add(exp1,exp2)  -> Int(to_int (evExp env exp1) + to_int(evExp env exp2)) 
  | Mul(exp1,exp2)  -> Int(to_int (evExp env exp1) * to_int(evExp env exp2)) 
  | Prefix(exp1,exp2) -> List(evExp env exp1::to_list (evExp env exp2)) 
  | Sign exp -> Int (- to_int (evExp env exp))
  | List exp_seq -> evExpSeq env exp_seq
  | Var id -> look_up id env
  | Num i -> Int i

and 

  evExpSeq env=function 
    SingleExp exp -> List [evExp env exp]
  | MoreExp(exp,exp_seq) -> List (evExp env exp::to_list (evExpSeq env exp_seq));;


let rec evStmt env=function
    Assign(id,exp) -> update env (evExp env exp) id 
  | VarStmt(id,exp) -> dec env (evExp env exp) id 
  | Print exp -> println(evExp env exp); env
  | For(id,exp,stmt_seq) ->  let lv=to_list (evExp env exp) in 
    List.fold_left (fun prev_env v -> exit_level(evStmtSeq (dec (enter_level prev_env) v id) stmt_seq)) env lv

and 

  evStmtSeq env=function 
    SingleStmt stmt -> evStmt env stmt
  | MoreStmt(stmt,stmt_seq) -> evStmtSeq (evStmt env stmt) stmt_seq;;

let evProg = function Prog stmt_seq -> try let _=evStmtSeq initial_env stmt_seq in () with Failure msg -> raise (EvalError msg) | exc -> raise exc ;;


(* some simple tests with the static semantics *)

(* difference between variable declaration and assignment 

prog1:
var x=0;x=x+1;print x // ok, prints 1

prog2:
x=0;x=x+1;print x // error, undefined variable x

*)

let stmt1=VarStmt(Id "x",Num 0);;

let stmt2=Assign(Id "x",Add(Var(Id "x"),Num 1));;

let stmt3=Print(Var(Id "x"));;

let prog1=Prog(MoreStmt(stmt1,(MoreStmt(stmt2,SingleStmt stmt3))));;

wfProg prog1;;

evProg prog1;;

print_newline();;

let stmt1=Assign(Id "x",Num 0);;

let prog2=Prog(MoreStmt(stmt1,(MoreStmt(stmt2,SingleStmt stmt3))));;

try wfProg prog2 with TypeError msg -> print_string ("Type error: "^msg^"\n\n");;

try evProg prog2 with EvalError msg -> print_string ("Eval error: "^msg^"\n\n");;

(* nested scopes

prog1:

var x=[1,2,3];for y:x {print y};y=0 // undefined variable y

prog2:

var x=[1,2,3];for y:x {x=y::x};print x // prints [ 3 2 1 1 2 3 ]

*)

let stmt1=VarStmt(Id "x",List(MoreExp(Num 1,MoreExp(Num 2,SingleExp(Num 3)))));;

let stmt2=For(Id "y",Var(Id "x"),SingleStmt(Print(Var(Id "y"))));;

let stmt3=Assign(Id "y",Num 0);;

let prog1=Prog(MoreStmt(stmt1,(MoreStmt(stmt2,SingleStmt stmt3))));;

try wfProg prog1 with TypeError msg -> print_string ("Type error: "^msg^"\n\n");;

try evProg prog1 with EvalError msg -> print_string ("Eval error: "^msg^"\n\n");;

let stmt1=VarStmt(Id "x",List(MoreExp(Num 1,MoreExp(Num 2,SingleExp(Num 3)))));;

let stmt2=For(Id "y",Var(Id "x"),SingleStmt(Assign(Id "x",Prefix(Var(Id "y"),Var(Id "x")))));;

let stmt3=Print(Var(Id "x"));;

let prog2=Prog(MoreStmt(stmt1,(MoreStmt(stmt2,SingleStmt stmt3))));;

wfProg prog2;;

evProg prog2;;

print_newline();;

(* variable hiding in nested scopes 
prog: 
var x=[1,2,3];for x:x {print x};x=0::x;print x 
// prints 
1
2
3
[ 0 1 2 3 ]
*)

let stmt1=VarStmt(Id "x",List(MoreExp(Num 1,MoreExp(Num 2,SingleExp(Num 3)))));;

let stmt2=For(Id "x",Var(Id "x"),SingleStmt(Print(Var(Id "x"))));;

let stmt3=Assign(Id "x",Prefix(Num 0,Var(Id "x")));;

let stmt4=Print(Var(Id "x"));;

let prog=Prog (MoreStmt(stmt1,(MoreStmt(stmt2,(MoreStmt(stmt3,SingleStmt stmt4))))));;

wfProg prog;;

evProg prog;;

print_newline();;
