(ns clj-pnm.core
  (:require [clojure.string :as st]
            [clojure.java.io :as io])
  (:import (java.io Writer)))


;; String manipulation

(defn starts-with?
  [s c]
  (st/starts-with? s c))

(defn split
  [s r]
  (st/split s r))

(defn trim
  [s]
  (st/trim s))

(defn rplc
  [s f r]
  (st/replace s f r))

(defn blank?
  [s]
  (st/blank? s))

(defn lower-case
  [s]
  (st/lower-case s))

(defn read-str
  [s]
  (read-string s))

(defn split-on-space-char
  [s]
  (split s #"\s"))


;; Reading

(defn third
  [v]
  (nth v 2))

(defn fourth
  [v]
  (nth v 3))

(defn comment-line?
  [line]
  (starts-with? line "#"))

(defn read-file
  [f]
  (with-open [reader (io/reader f)]
    (into [] (remove #(or (empty? %)
                          (blank? %)) (line-seq reader)))))

(defn normalize-whitespace
  [v]
  (mapv (fn [s] (trim (rplc s #"\s+" " "))) v))

(defn tokenize
  [v]
  (flatten (map split-on-space-char v)))

(defn remove-comments
  [v]
  (remove comment-line? v))

(defn get-comments
  [v]
  (map (comp trim #(rplc % "#" "")) (filter comment-line? v)))

(defmulti parse
  (fn [v] (-> v
             first
             lower-case
             keyword)))

(defmethod parse :p1
  [v]
  (let [w (read-str (second v))
        h (read-str (third v))
        m (loop [i h
                 r []
                 s (drop 3 v)]
            (if (zero? i)
              r
              (recur (dec i)
                     (conj r (mapv read-str (take w s)))
                     (drop w s))))]
    {:type   :p1
     :width  w
     :height h
     :map    m}))

(defmethod parse :p2
  [v]
  (let [w (read-str (second v))
        h (read-str (third v))
        mv (read-str (fourth v))
        m (loop [i h
                 r []
                 s (drop 4 v)]
            (if (zero? i)
              r
              (recur (dec i)
                     (conj r (mapv read-str (take w s)))
                     (drop w s))))]
    {:type      :p2
     :width     w
     :height    h
     :max-value mv
     :map       m}))

(defmethod parse :p3
  [v]
  (let [w (read-str (second v))
        h (read-str (third v))
        mv (read-str (fourth v))
        m (loop [i h
                 r []
                 s (drop 4 v)]
            (if (zero? i)
              r
              (recur (dec i)
                     (conj r (into [] (partition 3 (map read-str (take (* w 3) s)))))
                     (drop (* w 3) s))))]
    {:type      :p3
     :width     w
     :height    h
     :max-value mv
     :map       m}))

(defmethod parse :default
  [v]
  :invalid-file-format)

(defn read-pnm
  [pnm]
  (-> pnm
      read-file
      remove-comments
      normalize-whitespace
      tokenize
      parse))


;; Writing

(defn writeln
  [^Writer writer ^String line]
  (when line
    (.write writer (str line "\n"))))

(defn write-comments
  [^Writer writer comments]
  (when comments
    (doseq [c comments]
      (writeln writer (str "# " c)))))

(defn write-map
  [^Writer writer m]
  (when m
    (doseq [l m]
      (writeln writer (st/join " " (flatten l))))))

(defn write-pnm
  ([pnm file-name] (write-pnm pnm file-name nil))
  ([pnm file-name comments]
   (let [f (io/file file-name)]
     (with-open [writer (io/writer (io/file file-name) :append true)]
       (doto writer
         (writeln (some-> pnm :type name st/upper-case))
         (writeln (-> pnm :width))
         (writeln (-> pnm :height))
         (writeln (-> pnm :max-value))
         (write-comments comments)
         (write-map (-> pnm :map)))
       f))))
