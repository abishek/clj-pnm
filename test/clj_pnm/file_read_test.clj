(ns clj-pnm.file-read-test
  (:require [clojure.test :refer :all]
            [clj-pnm.core :refer :all]
            [clojure.java.io :as io]))

(deftest pbm-read-test
  (testing "successfully read a pbm file"
    (is (= {:type   :p1
            :width  6
            :height 10
            :map    [[0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [1 0 0 0 1 0]
                     [0 1 1 1 0 0]
                     [0 0 0 0 0 0]
                     [0 0 0 0 0 0]]}
           (read-pnm (io/file "./test/resources/j.pbm")))))
  (testing "successfully read a pbm file with random whitespace characters"
    (is (= {:type   :p1
            :width  6
            :height 10
            :map    [[0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [0 0 0 0 1 0]
                     [1 0 0 0 1 0]
                     [0 1 1 1 0 0]
                     [0 0 0 0 0 0]
                     [0 0 0 0 0 0]]}
           (read-pnm (io/file "./test/resources/j-ws.pbm"))))))

(deftest pgm-read-test
  (testing "successfully read a pgm file"
    (is (= {:type      :p2
            :width     24
            :height    7
            :max-value 15
            :map       [[0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 3 3 3 3 0 0 7 7 7 7 0 0 11 11 11 11 0 0 15 15 15 15 0]
                        [0 3 0 0 0 0 0 7 0 0 0 0 0 11 0 0 0 0 0 15 0 0 15 0]
                        [0 3 3 3 0 0 0 7 7 7 0 0 0 11 11 11 0 0 0 15 15 15 15 0]
                        [0 3 0 0 0 0 0 7 0 0 0 0 0 11 0 0 0 0 0 15 0 0 0 0]
                        [0 3 0 0 0 0 0 7 7 7 7 0 0 11 11 11 11 0 0 15 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]]}
           (read-pnm (io/file "./test/resources/feep.pgm")))))
  (testing "successfully read a pgm file with random whitespace characters"
    (is (= {:type      :p2
            :width     24
            :height    7
            :max-value 15
            :map       [[0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 3 3 3 3 0 0 7 7 7 7 0 0 11 11 11 11 0 0 15 15 15 15 0]
                        [0 3 0 0 0 0 0 7 0 0 0 0 0 11 0 0 0 0 0 15 0 0 15 0]
                        [0 3 3 3 0 0 0 7 7 7 0 0 0 11 11 11 0 0 0 15 15 15 15 0]
                        [0 3 0 0 0 0 0 7 0 0 0 0 0 11 0 0 0 0 0 15 0 0 0 0]
                        [0 3 0 0 0 0 0 7 7 7 7 0 0 11 11 11 11 0 0 15 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]]}
           (read-pnm (io/file "./test/resources/feep-ws.pgm"))))))

(deftest pnm-read-test
  (testing "successfully read a ppm file"
    (is (= {:type      :p3
            :width     3
            :height    2
            :max-value 255
            :map       [['(255 0 0) '(0 255 0) '(0 0 255)]
                        ['(255 255 0) '(255 255 255) '(0 0 0)]]}
           (read-pnm (io/file "./test/resources/rgb.ppm")))))
  (testing "successfully read a ppm file with random whitespace characters"
    (is (= {:type      :p3
            :width     3
            :height    2
            :max-value 255
            :map       [['(255 0 0) '(0 255 0) '(0 0 255)]
                        ['(255 255 0) '(255 255 255) '(0 0 0)]]}
           (read-pnm (io/file "./test/resources/rgb-ws.ppm"))))))

(deftest read-comments-test
  (testing "read one comment line"
    (is (= '("This is an example bitmap of the letter \"J\"")
           (get-comments (read-file "./test/resources/j.pbm")))))
  (testing "read multiple comment lines"
    (is (= '("The part above is the header"
              "\"P3\" means this is a RGB color image in ASCII"
              "\"3 2\" is the width and height of the image in pixels"
              "\"255\" is the maximum value for each color"
              "The part below is image data: RGB triplets")
           (get-comments (read-file "./test/resources/rgb.ppm"))))))
