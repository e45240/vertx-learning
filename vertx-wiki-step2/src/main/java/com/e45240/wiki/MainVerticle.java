package com.e45240.wiki;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future<String> dbVerticleDeployment = Future.future();
        vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment);

        dbVerticleDeployment.compose(id -> {
            Future<String> httpVerticleDeployment = Future.future();
            vertx.deployVerticle(HttpServerVerticle.class,
                    new DeploymentOptions().setInstances(2), httpVerticleDeployment);
            return httpVerticleDeployment;
        }).setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(MainVerticle.class.getName());
    }
}
