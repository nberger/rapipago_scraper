(ns rapipago-search.provinces
  (require [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]
           [rapipago-search.util :refer :all]))

(defn- fetch-home
  "Fetches the home page for RapiPago"
  []
  (html/html-resource
    (java.io.StringReader.
      (:body (http/get "http://www.rapipago.com.ar/rapipagoWeb/index.htm")))))

(defn- province-options
  []
  (html/select (fetch-home) [:select#provinciaSuc :option]))

(defn provinces
  "List of provinces where RapiPago has shops"
  []
  (map parse-option
       (filter is-option-present? (province-options))))
