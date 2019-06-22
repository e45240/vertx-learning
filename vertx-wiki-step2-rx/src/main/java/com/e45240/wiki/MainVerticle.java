package com.e45240.wiki;

import io.vertx.core.*;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

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
        Vertx.vertx(new VertxOptions().setMetricsOptions(
                new DropwizardMetricsOptions()
                        .setEnabled(true)
                        .setJmxEnabled(true)
                        .setJmxDomain("vertx-metrics")
        )).deployVerticle(MainVerticle.class.getName());
    }
}
