(ns rapipago-scraper.provinces
  (require [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]
           [clojure.string :as string]
           [rapipago-scraper.util :refer :all]))

(defn- fetch-home
  "Fetches the home page for RapiPago"
  []
  (html/html-snippet
    (:body (http/get "http://www.rapipago.com.ar/rapipagoWeb/index.htm"))))

(defn- province-options
  []
  (html/select (fetch-home) [:select#provinciaSuc :option]))

(defn find-all
  "All provinces where RapiPago has shops"
  []
  (map parse-option
       (rest (province-options))))

(defn find-by-name
  [province-name]
  (filter #(= (string/lower-case province-name)
              (string/lower-case (:name %1)))
          (find-all)))

(comment

  (find-all)
  (find-by-name "Corrientes")

  )
