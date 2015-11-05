(defproject pissaz "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3211"]
                 [reagent "0.5.0"]
                 [re-frame "0.4.1"]
                 [secretary "1.2.3"]
                 [lib-noir "0.9.9"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [http-kit "2.1.18"]
                 [selmer "0.9.1"]
                 [com.ashafa/clutch "0.4.0"]
                 [cljs-ajax "0.3.14"]
                 [com.stuartsierra/component "0.3.0"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.3" :exclusions [cider/cider-nrepl]]  ]

  :main pissaz.core

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"  ]

  :resource-paths ["resources"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]

                        :figwheel {:on-jsload "pissaz.core/mount-root"}

                        :compiler {:main pissaz.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "min"
                        :source-paths ["src/cljs"]
                        :compiler {:main pissaz.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false}}]})
