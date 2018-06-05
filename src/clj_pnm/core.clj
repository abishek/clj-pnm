(ns clj-pnm.core
  (:require [clojure.string :as st]
            [clojure.java.io :as io])
  (:import (java.io Writer)))


;; Reading

(defn comment-line?
  [line]
  (st/starts-with? line "#"))

(defn split-on-space-char
  [s]
  (st/split s #"\s"))

(defn third
  [v]
  (nth v 2))

(defn fourth
  [v]
  (nth v 3))

(defn read-file
  [f]
  (with-open [reader (io/reader f)]
    (into [] (remove #(or (comment-line? %) (empty? %)) (line-seq reader)))))

(defn normalize-whitespace
  [v]
  (mapv (fn [s] (st/trim (st/replace s #"\s+" " "))) v))

(defn tokenize
  [v]
  (flatten (map split-on-space-char v)))

(defmulti parse
          (fn [v] (-> v
                      first
                      st/lower-case
                      keyword)))

(defmethod parse :p1
  [v]
  (let [w (read-string (second v))
        h (read-string (third v))
        m (loop [i h
                 r []
                 s (drop 3 v)]
            (if (zero? i)
              r
              (recur (dec i)
                     (conj r (mapv read-string (take w s)))
                     (drop w s))))]
    {:type   :p1
     :width  w
     :height h
     :map    m}))

(defmethod parse :p2
  [v]
  (let [w (read-string (second v))
        h (read-string (third v))
        mv (read-string (fourth v))
        m (loop [i h
                 r []
                 s (drop 4 v)]
            (if (zero? i)
              r
              (recur (dec i)
                     (conj r (mapv read-string (take w s)))
                     (drop w s))))]
    {:type      :p2
     :width     w
     :height    h
     :max-value mv
     :map       m}))

(defmethod parse :p3
  [v]
  (let [w (read-string (second v))
        h (read-string (third v))
        mv (read-string (fourth v))
        m (loop [i h
                 r []
                 s (drop 4 v)]
            (if (zero? i)
              r
              (recur (dec i)
                     (conj r (into [] (partition 3 (map read-string (take (* w 3) s)))))
                     (drop (* w 3) s))))]
    {:type      :p3
     :width     w
     :height    h
     :max-value mv
     :map       m}))

(defmethod parse :default
  [v]
  (throw (Exception. (str "Unsupported file format " (first v) "!"))))

(defn read-ppm
  [ppm]
  (-> ppm
      read-file
      normalize-whitespace
      tokenize
      parse))


;; Writing

(defn writeln
  [^Writer writer ^String l]
  (.write writer (str l "\n")))

(defn writeln-not-empty
  [^Writer writer l]
  (when l
    (.write writer (str l "\n"))))

(defn write-comments
  [^Writer writer comments]
  (when comments
    (doseq [c comments]
      (writeln writer (str "# " c)))))

(defn write-map
  [^Writer writer m]
  (doseq [l m]
    (writeln writer (st/join " " (flatten l)))))

(defn write-ppm
  ([ppm file-name] (write-ppm ppm file-name nil))
  ([ppm file-name comments]
   (let [f (io/file file-name)]
     (with-open [writer (io/writer (io/file file-name) :append true)]
       (doto writer
         (writeln (-> ppm :type name st/upper-case))
         (writeln (-> ppm :width))
         (writeln (-> ppm :height))
         (writeln-not-empty (-> ppm :max-value))
         (write-comments comments)
         (write-map (-> ppm :map)))
       f))))
