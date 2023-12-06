package com.example.application.routes.cases

abstract class Case<T> {

    abstract fun handle( request:T);
}