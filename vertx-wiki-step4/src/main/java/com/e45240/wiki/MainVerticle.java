package com.e45240.wiki;

import com.e45240.wiki.database.WikiDatabaseVerticle;
import com.e45240.wiki.http.AuthInitializerVerticle;
import com.e45240.wiki.http.HttpServerVerticle;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Single<String> dbVerticleDeployment = vertx.rxDeployVerticle(new WikiDatabaseVerticle());
        dbVerticleDeployment
                .flatMap(id -> vertx.rxDeployVerticle(HttpServerVerticle.class.getName(),
                        new DeploymentOptions().setInstances(2)))
                .flatMap(id -> vertx.rxDeployVerticle(AuthInitializerVerticle.class.getName()))
                .subscribe(id -> startFuture.complete(), startFuture::fail);
    }
}
