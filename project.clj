(defproject jan25 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :source-paths ["src-clj"]
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "0.3.0"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild
  {:repl-listen-port 9000
   :repl-launch-commands
   {"firefox" ["/Applications/Firefox.app/Contents/MacOS/firefox" "-jsconsole" "http://localhost:8000"]}
   :builds [{:source-paths ["src-cljs"]
             :compiler {:output-to "resources/public/js/main.js"
                        :optimizations :whitespace
                        :pretty-print true}}]}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/google-closure-library-third-party "0.0-2029"]
                 [prismatic/dommy "0.0.1"]
                 [domina "1.0.1"]])
