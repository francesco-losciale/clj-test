(ns clj-test.library)

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

(defn fibonacci [s m]
  (if (= (count s) m)
    s
    (conj s (+ (nth s (dec (dec (count s)))) (last s)))
    ))

(= (fibonacci 3) '(0 1 1))
(= (fibonacci 6) '(0 1 1 2 3 5))
(= (fibonacci 8) '(0 1 1 2 3 5 8 13))


(defn function-in-library [param1]
  (str "Hello" param1))