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


(defn function-in-library [param1]
  (str "Hello" param1))