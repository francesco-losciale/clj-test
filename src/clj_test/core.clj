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
(defn print-details [person] (str (:name person) " " (:surname person)))
(= (print-details {:name "Francesco" :surname "Losciale"}))
(defn print-details [{:keys [name surname]}] (str name " " surname))
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
(= (partition 3 [1 2 3 4 5 6 7 8 9]) [[1 2 3] [4 5 6] [7 8 9]])
(= (partition 3 [1 2 3 4 5 6 7 8 9 10]) [[1 2 3] [4 5 6] [7 8 9]]) ; ignore extra element (10)
(= (partition-all 3 [1 2 3 4 5 6 7 8 9 10]) [[1 2 3] [4 5 6] [7 8 9] [10]]) ; ignore extra element (10)
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
  (:require [clojure.set :as set])
  (:import (java.net InetAddress)
           (clj_test.core BigMushroom)
           (sun.jvm.hotspot.ui EditableAtEndDocument)))
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

; polymorphism with protocols
(defprotocol BigMushroom
  (eat-mushroom [this]))

(extend-protocol BigMushroom
  java.lang.String
  (eat-mushroom [this]
    (str (.toUpperCase this) " mmm tasty!"))

  clojure.lang.Keyword
  (eat-mushroom [this]
    (case this
      :grow "Eat the right side"
      :shrink "Eat the left side"))

  java.lang.Long
  (eat-mushroom [this]
    (if (< this 3)
      "Eat the right side"
      "Eat the left side"))
  )
(= (eat-mushroom "wow!") "WOW! mmm tasty!")
(= (eat-mushroom :grow) "Eat the right side")
(= (eat-mushroom :shrink) "Eat the left side")

; what if we need to use strucutred data? we can use defrecord
(defrecord Mushroom [color height])
(def regular-mushroom (Mushroom. "white and polka dots" "2 inches"))
(= (class regular-mushroom) clj_test.core.Mushroom)
(= (.-color regular-mushroom) "white and polka dots")
(= (.-height regular-mushroom) "2 inches")

; let's create a protocol Edible that will be extended by mushrooms records
(defprotocol Edible
  (bite-right-side [this])
  (bite-left-side [this]))
(defrecord WonderlandMushroom [color height]
  Edible
  (bite-left-side [this]
    (str "The " color " bite makes you grow bigger"))
  (bite-right-side [this]
    (str "The " color " bite makes you grow smaller")))
(defrecord RegularMushroom [color height]
  Edible
  (bite-left-side [this]
    (str "The " color " bite taste bad"))
  (bite-right-side [this]
    (str "The " color " bite taste bad too")))
(def alice-mushroom (WonderlandMushroom. "blue dots" "3 inches"))
(def reg-mushroom (RegularMushroom. "brown" "1 inches"))
(bite-right-side alice-mushroom)
(bite-right-side reg-mushroom)
(bite-left-side alice-mushroom)
(bite-left-side reg-mushroom)


; let's use deftype because we don't care about color and height anymore
(defprotocol Edible
  (bite-right-side [this])
  (bite-left-side [this]))
(deftype WonderlandMushroom []
  Edible
  (bite-right-side [this]
    (str "The bite makes you grow bigger"))
  (bite-left-side [this]
    (str "The bite makes you grow smaller")))
(deftype RegularMushroom []
  Edible
  (bite-right-side [this]
    (str "The bite tastes bad"))
  (bite-left-side [this]
    (str "The bite tastes bad too")))
(def alice-mushroom (WonderlandMushroom.))
(def reg-mushroom (RegularMushroom.))
(bite-right-side alice-mushroom)
(bite-right-side reg-mushroom)
(bite-left-side alice-mushroom)
(bite-left-side reg-mushroom)


(defn bite-right-side [mushroom]
  (if (= (:type mushroom) "wonderland")
    "The bite makes you grow bigger"
    "The bite tastes bad"))
(defn bite-left-side [mushroom]
  (if (= (:type mushroom) "wonderland")
    "The bite makes you grow smaller"
    "The bite tastes bad"))
(bite-right-side {:type "wonderland"})
(bite-right-side {:type "regular"})





;;;;; other tests from 4clojure

(def arr '(1 2 3 4))

(defn penultimate [s]
  (nth (reverse s) 1))

(defn sum-all-seq [s]
  (reduce + s))

(defn filter-even [s]
  (filter even? s))

(defn palindrome [s]
  (= s (if (= (class s) java.lang.String)
         (clojure.string/reverse s)
         (reverse s))))

(defn dup-elem [s]
  (apply concat (map (partial repeat 2) s)))

;; rabbit hole here during exercise "Compress a Sequence".....
(reduce
  (fn [acc r]
    (if (= (last (str acc)) r)
      acc
      (str acc r)))
  "Leeeroy")
;; also
(reduce
  (fn [acc r]
    (prn "acc::" acc ", r::" r ", last acc::" (last acc))
    (if (= (last acc) r)
      acc
      (str acc r)))
  ""
  "Leeeroy")
;; real solution is
#(map first (partition-by identity %))

(defn drop-every-nth [l i]
  (vec (flatten (map (partial take (dec i)) (partition-all i l)))))

(defn replicate-n-times [l n]
  (apply concat (map (partial repeat n) l)))

;(defn fibonacci [s m]
;  (if (< (count s) m)
;    (case (count s)
;      0 (fibonacci [0] m)
;      1 (fibonacci [0 1] m)
;      (fibonacci
;       (conj s (+ (nth s (dec (dec (count s)))) (last s)))
;       m))
;    s))
;
;(defn fibonacci [s]
;  (case s
;    [] (fibonacci [0])
;    [0] (fibonacci [0 1])
;    (lazy-seq (fibonacci
;       (conj s (+ (nth s (dec (dec (count s)))) (last s)))))))

(defn fibonacci
  ([] (fibonacci 0 1))
  ([a b] (lazy-seq (cons a (fibonacci b (+ a b))))))


(= (take 3 (fibonacci)) '(0 1 1))
(= (take 6 (fibonacci)) '(0 1 1 2 3 5))
(= (take 8 (fibonacci)) '(0 1 1 2 3 5 8 13))

(defn caps [s]
  (apply str (filter #(Character/isUpperCase %) s)))
(= (caps "HeLlO, WoRlD!") "HLOWRD")

(= 6 (some #{2 7 6} [5 6 7 8]))
(= 6 (some #(when (even? %) %) [5 6 7 8]))


;In mathematics, the factorial of a positive integer n, denoted by n!,
; is the product of all positive integers less than or equal to n
(defn factorial [n]
  (if (> n 1)
    (* n (factorial (dec n)))
    1))

(= (factorial 1) 1)
(= (factorial 3) 6)
(= (factorial 5) 120)
(= (factorial 8) 40320)

(= [2 4] (let [[a b c d e] [0 1 2 3 4]] [c e]))
(= [1 2 [3 4 5] [1 2 3 4 5]] (let [[a b & c :as d] [1 2 3 4 5]] [a b c d]))

;(defn xor
;  ([a b] (= (count (filter true? [a b])) 1)))
;
;(defn not-all-true? [& all]
;  (reduce xor all))

(defn not-all-true? [& all]
  (true? (and (some true? all) (some false? all))))

(= false (not-all-true? false false))
(= true (not-all-true? true false))
(= false (not-all-true? true))
(= true (not-all-true? false true false))
(= false (not-all-true? true true true))
(= true (not-all-true? true true true false))

(defn multipliers [x n]
  (lazy-seq (cons (* x n) (multipliers x (inc n)))))

(defn lcm [a b]
  (apply min (clojure.set/intersection
               (set (take 100 (multipliers a 1)))
               (set (take 100 (multipliers b 1)))
               )))

(defn abs [n]
  (max n (- n)))

(defn gcd [a b]
  (/ (abs (* a b)) (lcm a b)))

(= (gcd 2 4) 2)
(= (gcd 10 5) 5)
(= (gcd 5 7) 1)
(= (gcd 1023 858) 33)



; Given a positive integer n, return a function (f x) which computes x^n.
; Observe that the effect of this is to preserve the value of n for use
; outside the scope in which it is defined.

(defn pow-n [n]
  (let [pow (fn pow [x n]
              (if (> n 0)
                (* x (pow x (dec n)))
                1))]
    (fn [x] (pow x n))))

(= 256 ((pow-n 2) 16), ((pow-n 8) 2))
(= [1 8 27 64] (map (pow-n 3) [1 2 3 4]))
(= [1 2 4 8 16] (map #((pow-n %) 2) [0 1 2 3 4]))



; Write a function which calculates the Cartesian product of two sets.

;(defn mul [x l]
;  (if (not (empty? (rest l)))
;    (conj [] (conj [] x (first l)) (mul x (rest l)))
;    (conj [] (conj [] x (first l)))))
;
;(defn cart-prod [s1 s2]
;  (for [el (into [] s1)]
;    (mul el (into [] s2))))
;
;(defn cart-prod [s1 s2]
;  (for [l (into [] s1)]
;    (for [r (into [] s2)]
;      [l r])))

(defn cart-prod [s1 s2]
  (into #{}
        (for [x s1 y s2] (vector x y))))

(= (cart-prod #{"ace" "king" "queen"} #{"♠" "♥" "♦" "♣"})
   #{["ace"   "♠"] ["ace"   "♥"] ["ace"   "♦"] ["ace"   "♣"]
     ["king"  "♠"] ["king"  "♥"] ["king"  "♦"] ["king"  "♣"]
     ["queen" "♠"] ["queen" "♥"] ["queen" "♦"] ["queen" "♣"]})

(= (cart-prod #{1 2 3} #{4 5})
   #{[1 4] [2 4] [3 4] [1 5] [2 5] [3 5]})

(= 300 (count (cart-prod (into #{} (range 10))
                  (into #{} (range 30)))))

; Write a function which returns the symmetric difference of two sets.
; The symmetric difference is the set of items belonging to one but not
; both of the two sets.

(defn symm-diff [s1 s2]
  (clojure.set/union
    (clojure.set/difference s1 (clojure.set/intersection s1 s2))
    (clojure.set/difference s2 (clojure.set/intersection s1 s2))))

(= (symm-diff #{1 2 3 4 5 6} #{1 3 5 7}) #{2 4 6 7})
(= (symm-diff #{:a :b :c} #{}) #{:a :b :c})
(= (symm-diff #{} #{4 5 6}) #{4 5 6})
(= (symm-diff #{[1 2] [2 3]} #{[2 3] [3 4]}) #{[1 2] [3 4]})


(defn multipliers [x n]
  (lazy-seq (cons (* x n) (multipliers x (inc n)))))

(defn lcm
  ([] 100)
  ([a b]
       (apply min (clojure.set/intersection
                    (set (take 100 (multipliers a 1)))
                    (set (take 100 (multipliers b 1))))
              ))
  ([a b c]
       (apply min (clojure.set/intersection
                    (set (take 100 (multipliers a 1)))
                    (set (take 100 (multipliers b 1)))
                    (set (take 100 (multipliers c 1)))
                    )
              ))
  ([a b c d]
   (apply min (clojure.set/intersection
                (set (take 1000 (multipliers a 1)))
                (set (take 1000 (multipliers b 1)))
                (set (take 1000 (multipliers c 1)))
                (set (take 1000 (multipliers d 1)))
                )
          )))

(== (lcm 2 3) 6)
(== (lcm 5 3 7) 105)
(== (lcm 1/3 2/5) 2)
(== (lcm 7 5/7 2 3/5) 210)
(== (lcm 3/4 1/6) 3/2)
