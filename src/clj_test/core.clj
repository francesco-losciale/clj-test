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

; partial functions
(defn add [l r]
  (+ l r))
(= (add 1 2) 3)
(def addOne (partial add 1))
(= (addOne 2) 3)
; comp functions
(defn subtract [l r]
  (- l r))
(= (subtract 1 2) -1)
(def subtractOne (partial subtract 1))
(not (= ((comp addOne subtractOne) 0) (subtractOne (addOne 0))))
(= ((comp addOne subtractOne) 0) (addOne (subtractOne 0)))
(= ((comp addOne subtractOne) 0) 0)

; destructuring
; vectors
(defn print-name [] (let [[name surname] ["Francesco" "Losciale"]]
   (str name " " surname)))
(= (print-name) "Francesco Losciale")
; vectors using :as
(defn print-name [] (let [[name surname :as name-and-surname] ["Francesco" "Losciale"]]
                      (str name " " surname " from array " name-and-surname)))
(= (print-name) "Francesco Losciale from array [\"Francesco\" \"Losciale\"]")
; maps
(defn print-name [] (let [{name :name surname :surname} {:name "Francesco" :surname "Losciale"}]
                      (str name " " surname)))
(= (print-name) "Francesco Losciale")
; maps using :or for default values
(defn print-name [] (let [{name :name surname :surname :or {surname "missing"}} {:name "Francesco"}]
                      (str name " " surname)))
(= (print-name) "Francesco missing")
; maps using :as
(defn print-name [] (let [{name :name surname :surname :as name-and-surname} {:name "Francesco" :surname "Losciale"}]
                      (str name " " surname " from map " name-and-surname)))
(= (print-name) "Francesco Losciale from map {:name \"Francesco\", :surname \"Losciale\"}")
; using :keys to extract keys from a map
(defn print-name [] (let [{:keys [name surname]} {:name "Francesco" :surname "Losciale"}]
                      (str name " " surname)))
(= (print-name) "Francesco Losciale")
; destructuring on function parameter
(defn print-details[person] (str (:name person) " " (:surname person)))
(= (print-details {:name "Francesco" :surname "Losciale"}))
(defn print-details[{:keys [name surname]}] (str name " " surname))
(= (print-details {:name "Francesco" :surname "Losciale"}))

; lazy sequence
(= (range 3) '(0 1 2))
(= (take 1 (range 3)) '(0))
(= (count (take 10 (range 1000))) 10)
(= (repeat 3 "abc") '("abc" "abc" "abc"))                   ; repeat returns another lazy seq
(= (take 1 (repeat 3 "abc")) '("abc"))
(= (rand-int 10))
; below: it does not create sequence of infinite random numbers.
; it first evaluates rand-int then repeats the result
(= (repeat 5 (rand-int 10)))
; below: it does execute an high-order function repeatedly
(= (repeatedly 5 #(rand-int 10)))

; recursion
(def adjs ["normal" "too small" "too big" "swimming"])
(def expected-result ["Alice is normal" "Alice is too small" "Alice is too big" "Alice is swimming"])
(defn alice-is [adjs result]
  (if (empty? adjs)
    result
    (alice-is (rest adjs) (conj result (str "Alice is " (first adjs))))
    ))
(= (alice-is adjs []) expected-result)
; recursion using loop - allows to remove second parameter and implements tail recursion
(def adjs ["normal" "too small" "too big" "swimming"])
(def expected-result ["Alice is normal" "Alice is too small" "Alice is too big" "Alice is swimming"])
(defn alice-is [adjs]
  (loop [input adjs
         result []] (if (empty? input)
     result
     (recur (rest input) (conj result (str "Alice is " (first input))))
     )))
(= (alice-is adjs) expected-result)

; map
(= (map #(str %) [1 2 3]) '("1" "2" "3"))
; returns a LazySeq so it doesn't evaluate impure functions!
(def print-nums (map #(println %) [1 2 3]))
print-nums                                                  ; only when evaluated it prints nums
; force evaluation of side effects with doall
(def print-nums (doall (map #(println %) [1 2 3])))
; map can take two collections
(def names ["Francesco" "Giovanni"])
(def surnames ["L" "M"])
(defn print-names [name surname]
  (str name " " surname "."))
(= (map print-names names surnames) ["Francesco L." "Giovanni M."])
; map terminates at the shortest collection
(def names ["Francesco" "Giovanni" "Pasquale" "Maria"])
(def surnames ["L" "M"])
(defn print-names [name surname]
  (str name " " surname "."))
(= (map print-names names surnames) ["Francesco L." "Giovanni M."])

; reduce can't accept infinite sequences
; reduce takes a binary operation (or function with two arguments)
(= (reduce + [1 2 3]) 6)
(= (reduce (fn [x y] (+ x y)) [1 2 3]) 6)
(= (reduce (fn [x y] (if (even? y) (+ x y) y)) [1 2 3]) 3)

; data transformation
(= ((complement nil?) nil) (not (nil? nil)))
(= ((complement nil?) 1) (not (nil? 1)))
(= (filter (complement nil?) [nil :key :blah nil :bleah]) [:key :blah :bleah])
(= (filter keyword? [nil :key :blah nil :bleah]) [:key :blah :bleah])
(= (remove nil? [nil :key :blah nil :bleah]) [:key :blah :bleah])
(= (for [animal [:cow :rabbit :dog]] (str (name animal))) '("cow" "rabbit" "dog"))
(= (for [animal [:cow :rabbit :dog]
         color ["red" "green" "blue"]] (str (name animal) " " (name color))) '("cow red" "cow green" "cow blue" "rabbit red" "rabbit green" "rabbit blue" "dog red" "dog green" "dog blue"))
(= (for [animal [:cow :rabbit :dog]
         color ["red" "green" "blue"]
         :let [print-out (str (name animal) " " (name color))]]
        print-out) '("cow red" "cow green" "cow blue" "rabbit red" "rabbit green" "rabbit blue" "dog red" "dog green" "dog blue"))
(= (for [animal [:cow :rabbit :dog]
         color ["red" "green" "blue"]
         :let [print-out (str (name animal) " " (name color))]
         :when (= animal :cow)]
     print-out) '("cow red" "cow green" "cow blue"))
(= (flatten [1 2 [3 4 [5]]]) [1 2 3 4 5])
(= (flatten [1 2 [3 4 [5 {6 7}]]]) [1 2 3 4 5 {6 7}])
(= (vec '(1 2 3)) [1 2 3])                                  ; from list to vector
(= (into [] '(1 2 3)) [1 2 3])                              ; from list to vector
(= (sorted-map :b 2 :a 1 :z 3) {:a 1 :b 2 :z 3})
(= (into (sorted-map) {:b 2 :a 1 :z 3}) {:a 1 :b 2 :z 3})
(= (into {} [[:b 2] [:a 1] [:z 3]]) {:a 1 :b 2 :z 3})
(= (into [] {:a 1 :b 2 :z 3}) [[:a 1] [:b 2] [:z 3]])
(= (partition 3 [1 2 3 4 5 6 7 8 9]) [[1 2 3] [4 5 6] [ 7 8 9]])
(= (partition 3 [1 2 3 4 5 6 7 8 9 10]) [[1 2 3] [4 5 6] [ 7 8 9]]) ; ignore extra element (10)
(= (partition-all 3 [1 2 3 4 5 6 7 8 9 10]) [[1 2 3] [4 5 6] [ 7 8 9] [10]]) ; ignore extra element (10)
(= (partition-by #(= 6 %) [1 2 3 4 5 6 7 8 9]) [[1 2 3 4 5] [6] [7 8 9]])

; state management (idiomatic to use ! for impure functions)
(def who-atom (atom :caterpillar))
(= @who-atom :caterpillar)
(reset! who-atom :another-one)
(= @who-atom :another-one)
(reset! who-atom :caterpillar)
(defn change [state]
  (case state
    :caterpillar :chrysalis
    :chrysalis :butterfly
    :butterfly))
(= (swap! who-atom change) :chrysalis)
(= (swap! who-atom change) :butterfly)
(= (swap! who-atom change) :butterfly)
; swap uses retries so you shouldn't run side effects with it!!!!!!
(dotimes [n 5] (println n))
(def counter (atom 0))
(swap! counter inc)
(= @counter 1)
(swap! counter inc)
(swap! counter inc)
(= @counter 3)
(dotimes [_ 10] (swap! counter inc))
(= @counter 13)
; concurrency
(def counter (atom 0))
(= @counter 0)
(let [n 5]
  (future (dotimes [_ n] (swap! counter inc)))
  (future (dotimes [_ n] (swap! counter inc)))
  (future (dotimes [_ n] (swap! counter inc)))
  )
(= @counter 15)
; let add a side effect to print the numbers
(def counter (atom 0))
(= @counter 0)
(defn inc-and-print [val]
  (println val)
  (inc val))
(let [n 2]
  (future (dotimes [_ n] (swap! counter inc-and-print)))
  (future (dotimes [_ n] (swap! counter inc-and-print)))
  (future (dotimes [_ n] (swap! counter inc-and-print)))
  )
(= @counter 6)

; state management and concurrency
; we need to change two states in the same transaction
(def alice-height (ref 3))                                  ; increases by 24
(def right-hand-bites (ref 10))                             ; decreases
(defn eat-from-right-hand []
  (when (pos? @right-hand-bites)
    (alter right-hand-bites dec)
    (alter alice-height #(+ % 24))))
; below: will throw an exception, it is not in a transaction
(eat-from-right-hand)
(dosync (eat-from-right-hand))
; refactoring
(def alice-height (ref 3))                                  ; increases by 24
(def right-hand-bites (ref 10))                             ; decreases
(defn eat-from-right-hand []
  (dosync (when (pos? @right-hand-bites)
     (alter right-hand-bites dec)
     (alter alice-height #(+ % 24)))))
; let's run 3 thread two times = height should be 147
(let [n 2]
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand)))
  )
(= @alice-height 147)
(= @right-hand-bites 4)
; remember: alter! and swap! should NOT have side effects
; below: commute, similar to alter, but without retry.
(def alice-height (ref 3))                                  ; increases by 24
(def right-hand-bites (ref 10))                             ; decreases
(defn eat-from-right-hand []
  (dosync (when (pos? @right-hand-bites)
            (commute right-hand-bites dec)
            (commute alice-height #(+ % 24)))))
; let's run 3 thread two times = height should be 147
(let [n 2]
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand)))
  )
(= @alice-height 147)
(= @right-hand-bites 4)
; ref-set , example : y = x + 2 always
(def x (ref 1))
(def y (ref 2))
(defn new-values []
  (dosync
    (alter x inc)
    (ref-set y (+ 2 @x))))
(let [n 2]
  (future (dotimes [_ n] (new-values)))
  (future (dotimes [_ n] (new-values)))
  )
(= @x 5)
(= @y 7)

; agents - manage state asynchronously
(def who-agent (agent :caterpillar))
(= @who-agent :caterpillar)
(defn change [state]
  (case state
    :caterpillar :chrysalis
    :chrysalis :butterfly
    :butterfly))
(send who-agent change)                                     ; asynchronously
(= @who-agent :chrysalis)                                   ; this could not be true
(send-off who-agent change)                                 ; send-off to have extensible thread pool
(= @who-agent :butterfly)
(defn change-error [state]
  (throw (Exception. "Boom!")))
(send who-agent change-error)
(= @who-agent :caterpillar)                                 ; state did not change
(send who-agent change)                                     ; this throws an exception now
(agent-error who-agent)
; it will stay in broken state until we restart the agent
(restart-agent who-agent :caterpillar)
(send who-agent change)                                     ; it passes now
(= @who-agent :chrysalis)
;; when we don't want to break the state of the agent, but continue...
(def who-agent (agent :caterpillar))
(set-error-mode! who-agent :continue)
(defn err-handler-fn [a ex]
  (println "error" ex " value is " @a))
(set-error-handler! who-agent err-handler-fn)
; now sending the change-error will not break the state of the agent
(defn change-error [state]
  (throw (Exception. "Boom!")))
(send who-agent change-error)
(send who-agent change)                                     ; it still passes
(= @who-agent :chrysalis)


;; atom -> syncrhonous communication, not coordinated
;; ref  -> synchronous communication, coordinated
;; agent -> asynchronous communication, not coordinated


; java interoperability
(= (class "caterpillar") String)
(= (. "caterpillar" toUpperCase) "CATERPILLAR")
(= (.toUpperCase "caterpillar") "CATERPILLAR")
(= (.indexOf "caterpillar" "pillar") 5)
(= (new String "Hi") "Hi")
(= (String. "Hi") "Hi")
(ns clj-test.core
  (:import (java.net InetAddress)))
(= (.getHostName (InetAddress/getByName "localhost")) "localhost")
(= (.getHostName (java.net.InetAddress/getByName "localhost")) "localhost")
(def sb (doto (StringBuffer. "Who ")
   (.append "are ")
   (.append "you?")))
(= (.toString sb) "Who are you?")
(import 'java.util.UUID)
(UUID/randomUUID)

; polymorphism with multimethod
; dispatcher method is `class`
(defmulti who-are-you class)
(defmethod who-are-you java.lang.String [input]
  (str "String - who are you? " input))
(defmethod who-are-you clojure.lang.Keyword [input]
  (str "Keyword - who are you? " input))
(defmethod who-are-you java.lang.Long [input]
  (str "Long - who are you? " input))
(= (who-are-you "Fran") "String - who are you? Fran")
(= (who-are-you :fran) "Keyword - who are you? :fran")
(= (who-are-you 1) "Long - who are you? 1")
(= (who-are-you true))                                      ; IllegalArgumentException

; use custom dispatch method
(defmulti eat-mushroom (fn [height]
                         (if (< height 3)
                           :grow
                           :shrink)))
(defmethod eat-mushroom :grow [_]
  "Eat the right side to grow.")
(defmethod eat-mushroom :shrink [_]
  "Eat the left side to grow.")
(= (eat-mushroom 1) "Eat the right side to grow.")
(= (eat-mushroom 5) "Eat the left side to grow.")
