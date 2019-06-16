package com.e45240.wiki;

import com.e45240.wiki.database.WikiDatabaseVerticle;
import com.e45240.wiki.http.AuthInitializerVerticle;
import com.e45240.wiki.http.HttpServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future<String> dbVerticleDeployment = Future.future();
        vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment);
        dbVerticleDeployment.compose(id -> {
            Future<String> authInitDeployment = Future.future();
            vertx.deployVerticle(new AuthInitializerVerticle(), authInitDeployment);
            return authInitDeployment;
        }).compose(id -> {
            Future<String> httpVerticleDeployment = Future.future();
            vertx.deployVerticle(HttpServerVerticle.class,
                    new DeploymentOptions().setInstances(1),
                    httpVerticleDeployment);
            return httpVerticleDeployment;
        }).setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }
}
