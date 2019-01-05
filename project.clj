(defproject clj-pnm "0.1.0-SNAPSHOT"
  :url "http://theparanoidtimes.org/projects/clj-pnm"
  :description "A Clojure(Script) library for reading and writing Netpbm format."
  :license {:name         "The MIT License"
            :distribution :repo
            :url          "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]]
  :plugins [[lein-figwheel "0.5.16"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]
  :source-paths ["src"]
  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :compiler {:main clj-pnm.core
                           :output-to "target/js/main.js"
                           :output-dir "target/js"
                           :source-map-timestamp true
                           :pretty-print true
                           :preloads [devtools.preload]}}
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "target/js/main.min.js"
                           :main clj-pnm.core
                           :optimizations :advanced
                           :pretty-print false}}]}
  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.9"]
                                  [figwheel-sidecar "0.5.16"]
                                  [cider/piggieback "0.3.1"]]
                   :source-paths ["src"]
                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                   :clean-targets ^{:protect false} ["target/js"
                                                     :target-path]}})
