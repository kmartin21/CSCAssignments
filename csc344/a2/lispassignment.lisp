(setq p1 '(+ x (* x (- y (/ z 2)))))
(setq p2 '(+ (- z 2) (* x 5)))
(setq p3 '(+ 1 a))

(defun addexp (e1 e2) (list '+ e1 e2))
(defun subexp (e1 e2) (list '- e1 e2))
(defun mulexp (e1 e2) (list '* e1 e2))
(defun divexp (e1 e2) (list '/ e1 e2))

(defun deep-subst (old new l)
  (cond 
 
   ((null l) 
     nil
   )
    
   ((listp (car l))
 
    (cons (deep-subst old new (car l)) (deep-subst old new (cdr l)))
   )
 
   ((eq old (car l)) 
 
    (cons new (deep-subst old new (cdr l)))
   )
 
   (T   
 
    (cons (car l) (deep-subst old new (cdr l)))
   )
  )
)

(defun subst-bindings (bindinglist exp)
      (cond 
 
        ( (null bindinglist) 
           exp )
        (T
 
           (subst-bindings (cdr bindinglist) (deep-subst (car (car bindinglist)) (car (cdr (car bindinglist))) exp))
        )
       )
     )

(defun simplify-triple(op left-arg right-arg)
  (cond
    ((and (numberp left-arg) (numberp right-arg))
      (eval (list op left-arg right-arg))
    )
 
    ((and (eq op '+) (eql right-arg 0))
      left-arg
    )
    ((and (eq op '+) (eql left-arg 0))
      right-arg
    )
    
    ((and (eq op '-) (eql right-arg 0))
      left-arg
    )
    ((and (eq op '-) (eql right-arg left-arg))
      0
    )
 
   ((and (eq op '+) (listp right-arg) (eq (car right-arg) '-) (eql (car (cdr right-arg)) 0))
      0
    )
    ((and (eq op '*) (eql right-arg 0))
      0
    )
    ((and (eq op '*) (eql left-arg 0))
      0
    )
    ((and (eq op '*) (eql right-arg 1))
      left-arg
    )
    ((and (eq op '*) (eql left-arg 1))
      right-arg
    )
    ((and (eq op '/) (eql left-arg 0))
      0
    )
    ((and (eq op '/) (eql right-arg 1))
      left-arg
    )
    ((and (eq op '/) (eql right-arg left-arg))
      1
    )

    
    (T
 
      (list op left-arg right-arg))))


(defun simplify (exp)

 (cond
 
   ( (listp exp)
 
    (simplify-triple (car exp) (simplify (car (cdr exp))) (simplify (car (cdr (cdr exp)))))
) 
(T 
 
       exp)))

(defun evalexp (bindinglist exp)
  
  (simplify (subst-bindings bindinglist exp)
))

