(ns clj-pnm.write-file-test
  (:require [clojure.test :refer :all]
            [clj-pnm.core :refer :all]
            [clojure.java.io :as io])
  (:import (java.io File)))

(defn with-test-dir
  [f]
  (let [^File d (io/file "./t")]
    (.mkdirs d)
    (f)
    (doseq [fl (reverse (file-seq d))]
      (.delete fl))
    (.delete d)))

(use-fixtures :once with-test-dir)

(deftest pbm-write-test
  (testing "successfully write a pbm map to a file"
    (let [f (write-pnm {:type   :p1
                        :width  1
                        :height 1
                        :map    [[1]]} "./t/test1.pbm")]
      (is (= "P1\n1\n1\n1\n"
             (slurp f)))))
  (testing "successfully write a pbm map with comments to a file"
    (let [f (write-pnm {:type   :p1
                        :width  1
                        :height 1
                        :map    [[1]]} "./t/test2.pbm" #{"Comm1" "Comm2"})]
      (is (= "P1\n1\n1\n# Comm1\n# Comm2\n1\n"
             (slurp f))))))

(deftest pgm-write-test
  (testing "successfully write a pgm map to a file"
    (let [f (write-pnm {:type      :p2
                        :width     2
                        :height    2
                        :max-value 4
                        :map       [[1 2]
                                    [3 4]]} "./t/test1.pgm")]
      (is (= "P2\n2\n2\n4\n1 2\n3 4\n"
             (slurp f)))))
  (testing "successfully write a pgm map with comments to a file"
    (let [f (write-pnm {:type      :p2
                        :width     2
                        :height    2
                        :max-value 4
                        :map       [[1 2]
                                    [3 4]]} "./t/test2.pgm" #{"Comm1" "Comm 2"})]
      (is (= "P2\n2\n2\n4\n# Comm 2\n# Comm1\n1 2\n3 4\n"
             (slurp f))))))

(deftest ppm-write-test
  (testing "successfully write a ppm map to a file"
    (let [f (write-pnm {:type      :p3
                        :width     2
                        :height    2
                        :max-value 255
                        :map       [['(0 125 200) '(255 0 0)]
                                    ['(255 255 255) '(58 98 78)]]} "./t/test1.ppm")]
      (is (= "P3\n2\n2\n255\n0 125 200 255 0 0\n255 255 255 58 98 78\n"
             (slurp f)))))
  (testing "successfully write a ppm map with comments to a file"
    (let [f (write-pnm {:type      :p3
                        :width     2
                        :height    2
                        :max-value 255
                        :map       [['(0 125 200) '(255 0 0)]
                                    ['(255 255 255) '(58 98 78)]]} "./t/test2.ppm" #{"Comm 1" "Comm 2"})]
      (is (= "P3\n2\n2\n255\n# Comm 2\n# Comm 1\n0 125 200 255 0 0\n255 255 255 58 98 78\n"
             (slurp f))))))
