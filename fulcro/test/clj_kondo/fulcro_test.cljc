(ns clj-kondo.fulcro-test
  (:require
   #?(:cljs [com.fulcrologic.fulcro.dom :as dom]
      :clj  [com.fulcrologic.fulcro.dom-server :as dom])
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.mutations :refer [defmutation]]
   [com.fulcrologic.fulcro.routing.dynamic-routing :refer [defrouter]]
   [com.fulcrologic.guardrails.core :refer [>def >defn =>]]
   [com.fulcrologic.rad.attributes :refer [defattr]]
   [com.fulcrologic.rad.attribute-options :as ao]
   [com.fulcrologic.rad.authorization :refer [defauthenticator]]
   [clojure.spec.alpha :as s]))

(defattr id :uuid
  {ao/identity? true})

(defsc DestructuredExample [this
                            {:keys [db/id] :as props}
                            {:keys [onClick] :as computed :or {onClick identity}}]
  {:query         [:db/id]
   :initial-state {:db/id 22}}
  (dom/div
   (str "Component: " id)))

(defrouter MyRouter [this props] ;; unused binding 'this'/'props'
  {:router-targets [DestructuredExample]})

(defauthenticator MyAuthenticator {:local DestructuredExample})

(defmutation mutation-example1
  "Here is a doc string"
  [params]
  (action [] params) ;; handler 'action' should be a fn of 1 arg
  (remote [env] true) ;; unused binding 'env'
  )

(defmutation mutation-example2
  "Here is a doc string"
  [params] ;; unused binding params
  (action [_env] nil)
  (remote [_env] true))

(defmutation mutation-example2 ;; redefining var mutation-example2
  [params]
  (action [_env] nil)
  (remote [_env] true))

(>def person-name string?)

(>defn say-hello [person-name]
  [person-name => (s/or nil? string?)]
  (println (str "Hello, " person-name)))

(>defn say-hello2 [person-name address]
  [person-name => nil?] ;; error: guardrail spec does not match function signature
  (println (str "Hello, " person-name)))