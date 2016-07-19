(defproject rapipago_scraper "0.1.1-SNAPSHOT"
  :description "Rapipago stores scraper. Scrapes from www.rapipago.com"
  :url "http://github.com/nberger/rapipago_scraper"
  :scm {:name "git"
        :url "https://github.com/nberger/rapipage_scraper"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.1.0"]
                 [enlive "1.1.6"]]
  :profiles {:dev
             {:dependencies [[com.gfredericks/vcr-clj "0.4.6"]]}})
