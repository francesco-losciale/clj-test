(ns clj-test.core
  (:require
    ;[clj-test.library :ref :all]
    [clj-test.library :as lib]))

;(require '[clj-test.library :as lib])

; collections: persistent immutable data structure (clojure.lang.IPersistentCollection)

; lists (linked)
(= (last '(1 2 3)) 3)
(= (first '(1 2 3)) 1)
(= (first '(1 "2" 3)) 1)
(= (rest '(1 2 3)) '(2 3))
(= (rest (rest (rest (rest '(1 2 3))))) ())
(= (first (rest (rest (rest (rest '(1 2 3)))))) nil)
(= (cons 5 nil) '(5))
(= (cons 5 '()) '(5))
(= (cons 5 (cons 4 nil)) '(5 4))

; vectors (indexed list)
(= (last [:symbol 1 "str"]) "str")
(= (first [:symbol 1 "str"]) :symbol)
(= (rest [:symbol 1 "str"]) [1 "str"])
(= (nth [:symbol 1 "str"] 0) :symbol)
(= (nth [:symbol 1 "str"] 1) 1)
(= (nth [:symbol 1 "str"] 2) "str")

; conj behaves differently for lists/vectors
; for (linked) list it's more natural to add the element as first
; for vectors it is added at the end
(= (conj '(1 2 3) 4) '(4 2 3 1))
(= (conj [1 2 3] 4) [1 2 3 4])
(= (conj '(1 2) 3 4) '(4 3 1 2))

; maps
(= (get {:one "1" :two "2"} :one) "1")
(= ({:one "1" :two "2"} :one) "1")
(= (:one {:one "1" :two "2"}) "1")
(= (:three {:one "1" :two "2"} "not found") "not found")
(= (keys {:one "1" :two "2"}) '(:one :two))
(= (vals {:one "1" :two "2"}) '("1" "2"))
(= (assoc {:one "1" :two "2"} :three "3") {:one "1" :two "2" :three "3"})
(= (dissoc {:one "1" :two "2"} :two) {:one "1"})
(= (merge {:one "1" :two "2"} {:one "1" :two "two" :three "three"}) {:one "1" :two "two" :three "three"})

; sets (unique elements)
(= (get #{1 2 3 4 5 6} 1) 1)
(= (:1 #{:1 2 3 4 5 6}) :1)
(= (#{:1 2 3 4 5 6} :1) :1)
(= (contains? #{:1 2 3 4 5 6} :1) true)
(= (clojure.set/union #{1 2 3} #{4 5 6}) #{1 2 3 4 5 6})
(= (clojure.set/difference #{1 2 3 4 5 6} #{4 5 6}) #{1 2 3})
(= (clojure.set/intersection #{1 2 3 4} #{4 5 6}) #{4})
(= (set [1 2 3 4 5 6]) #{1 2 3 4 5 6})
(= (set [1 2 3 4 5 6 6 6 6]) #{1 2 3 4 5 6})
(= (set {:one "1" :two "2"}) #{[:one "1"] [:two "2"]})

; code is data
(= ((first '(+ 1 2 3)) 1 2) 3)                              ; <-- ?????

; vars
(def add +)
(= (add 1 2) 3)

(def user "Fran")
(= user "Fran")
(= (let [user1 "Fran"] user1) "Fran")
(= (let [user1 "Fran" user2 "Gian"] user2) "Gian")

; functions
(defn follow-the-rabbit [] "Off we go!")
(defn follow-the-rabbit [par1 par2] "Off we go!")
; anonymous functions
(fn [] "Off we go")
((fn [] "Off we go"))
#(str "Off we go" "!")
(#(str "Off we go" "!"))
#(str "Off we go" %1 "!")
(#(str "Off we go" %1 "!") " Fra")


(= (lib/function-in-library "Fran") "HelloFran")


; conditional logic
(true? true)
(false? false)
(nil? nil)
(not (nil? "d"))
(not (not true))
(not nil)
(not= (not "hi") true)

(empty? '())
(not (empty? '(1)))

; sequence: walkable list abstraction of collection
(seq [1 2 3])
(class (seq [1 2 3]))
(class [1 2 3])
(= (seq []) nil)

; don't use (not (empty? c)) to validate a collection is not empty
; nil is treated as logically false, so
; instead it's more idiomatic to use ...
(= (seq []) nil)

(every? odd? [3 5 7])
(not (every? odd? [3 5 7 6]))
(every? #(= % :drinkme) [:drinkme])
(every? (fn [x] (= x :drinkme)) [:drinkme])
(not (every? (fn [x] (= x :drinkme)) [:drinkme :poison]))
(= (not-any? (fn [x] (= x :drinkme)) [:drinkme :poison]) false)
(= (not-any? (fn [x] (= x :drinkme)) [:poison :poison]) true)
; some returns first true evaluation, nil otherwise
(some (fn [x] (= x :drinkme)) [:drinkme :poison])
; set is a function of its members
(= (#{1 2 3} 2) 2)
(= (some #{3} [1 2 3]) 3)
(= (some #{3 4} [1 2 3]) 3)
(= (some #{4 3 2} [1 2 3]) 2)
(= (some #{nil} [nil nil nil]) nil)
(= (some #{false} [false false false]) nil)

(= (if true "Hello" "Bye") "Hello")
; if & let vs if-let
(defn f1 [] (let [symbol-true true]
              (if symbol-true
                symbol-true
                false
                )))
(defn f2 [] (if-let [symbol-true true]
              symbol-true
              false
              ))
(= (f1) (f2))
(defn when-true [is-true]
  (when is-true
    "True"))
(= (when-true true) "True")
(= (when-true false) nil)
; when & let vs when-let
(defn f1 [] (let [symbol-true true]
              (when symbol-true
                symbol-true
                )))
(defn f2 [] (when-let [symbol-true true]
              symbol-true
              ))
(= (f1) (f2))

; cond instead of chain of if-elseif
(defn f [value]
  (cond (= value 1) "1"
        (= value 2) "2"
        ))
(= (f 1) "1")
(= (f 2) "2")
(= (f 3) nil)
; cond with default
(defn f [value]
  (cond (= value 1) "1"
        (= value 2) "2"
        :else "not found"
        ))
(= (f 1) "1")
(= (f 2) "2")
(= (f 3) "not found")
; cond default can be anything other than nil
(defn f [value]
  (cond (= value 1) "1"
        (= value 2) "2"
        "otherwise" "not found"
        ))
(= (f 1) "1")
(= (f 2) "2")
(= (f 3) "not found")

; use case with value comparison instead of cond
(defn f [value]
  (case value
    1 "1"
    2 "2"
    "not found"))
(= (f 1) "1")
(= (f 2) "2")
(= (f 3) "not found")


