:- dynamic currClass/1.
hasType(X,var) :- var(X), !.
hasType(a,class).
hasType(b,subClass).
hasType(add,expression) :- !.
hasType(strLength,method) :- !.
hasType(X,int) :- number(X).
hasType(X,string) :- atom(X).
hasClass(X,Y) :- method(X,Y) ; exp(X,Y).
hasMethod(X) :- currClass(Y), method(X,Z), extend(Y,Z).
hasExp(X) :- currClass(Y), exp(X,Z), extend(Y,Z).
class(a).
parentOf(b,a).
exp(add,a).
method(strLength,a).
extend(Curr,ParOf) :- =(Curr,ParOf) ; (class(Curr), =(Curr,ParOf)) ; (parentOf(Curr,X), extend(X,ParOf)).
strLength(X,int) :- atom(X), hasMethod(strLength).
add(X,Y,int) :- integer(X), integer(Y), hasExp(add).
class(X,Y) :- ((parentOf(X, _Z)) ; class(X)), assertz(currClass(X)), Y, retractall(currClass(X)).







