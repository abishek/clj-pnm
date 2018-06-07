# clj-pnm

[![Build Status](https://travis-ci.org/theparanoidtimes/clj-pnm.svg?branch=master)](https://travis-ci.org/theparanoidtimes/clj-pnm)

A Clojure library for reading and writing Netpbm files.

Reads a *pnm* file into a Clojure structure. That same structure
can be used to write a *pnm* file. Currently supports *p1* (*.pbm*),
*p2* (*.pgm*) and *p3* (*.ppm*) files.

Good explanation of the format and the documents that are used as guides
for this library can be found [here](http://netpbm.sourceforge.net/doc/ppm.html)
and [here](https://en.wikipedia.org/wiki/Netpbm_format).

The library is still in its early stages, so any feedback and support is
welcome.

## Usage

Read a *test.pbm* file

```
P1
2
2
1 1
1 1
```

``` clojure
(require '[clj-pnm.core :as pnm])
=> nil

(pnm/read-pnm (io/file "test.pnm"))
=> {:type :p1, :width 2, :height 2, :map [[1 1] [1 1]]}
```

Read a *test.pgm* file

```
P2
3
2
4
1 2 3
4 3 2
```

``` clojure
(pnm/read-pnm (io/file "test.pgm"))
=> {:type :p2, :width 3, :height 2, :max-value 4, :map [[1 2 3] [4 3 2]]}
```

Finally, read a *test.ppm* file

```
P3
2
2
255
255 0 255 128 52 123
0 0 0 45 45 45
```

``` clojure
(pnm/read-pnm (io/file "test.ppm"))
=> {:type :p3
    :width 2
    :height 2
    :max-value 255
    :map [[(255 0 255) (128 52 123)]
          [(0 0 0) (45 45 45)]]} ;; prettified
```

It can extract comments if necessary

```
P1
1
1
# A comment
# Another comment
1
```

``` clojure
(pnm/get-comments (pnm/read-file (io/file "comment.pbm")))
=> ("A comment" "Another comment")
```

Writing a file is just a matter of passing a map like the ones explained
above. It can include comments also which are always written after the header part.

``` clojure
(pnm/write-pnm {:type :p1 :width 1 :height 1 :map [[1]]} "out.pbm")
=> ...out.pbm file...

(slurp *1)
=> "P1
    1
    1
    1" ;; prettified

(pnm/write-pnm {:type :p3 :width 1 :height 1 :max-value 255:map [[1]]} "out.ppm", #{"A comment" "Another comment"})
=> ...out.ppm file...

(slurp *1)
=> "P3
    1
    1
    255
    # Another comment
    # A comment
    255 255 255" ;; prettified
```

## License

Copyright 2018, Dejan Josifović, the paranoid times

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
