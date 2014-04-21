(ns rapipago-search.core-test
  (:require [clojure.test :refer :all]
            [vcr-clj.clj-http :refer [with-cassette]]
            [rapipago-search.core :refer :all]))

(deftest search-without-filter
  (with-cassette :search-without-filter
    (def search-result (search {}))
    (is (= (count search-result) 6))
    (is (some #{"LOCUTORIOS INTERVISION"} (map :name search-result)))))
