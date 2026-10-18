# play-template

Starter for new **Play 3 + Scala** projects. This is `playfinal2` with all the application code stripped out — only the proven setup is kept, so a new project created from it starts from dependencies that already work together.

| | |
|---|---|
| Framework | Play 3.0 (`sbt-plugin` 3.0.12), sbt 1.12.15 |
| Language | Scala 2.13.18 |
| JSON | play-json (brought in by Play) |
| Storage | MongoDB 8 via `mongo-scala-driver` 5.13.0 — *driver declared, docker-compose ready, no code yet* |
| FP | cats-core 2.13.0 — *declared, no code yet* |
| Logging | scala-logging 3.9.5 + logback (`conf/logback.xml`) |
| DI | **Compile-time** (cake pattern), no Guice runtime wiring |

---

## Run it

```powershell
docker compose up -d
sbt run
```

Then hit http://localhost:9000/ → `Play up and running!` Test with `sbt test`.

---

## What's included

```
app/
  controllers/   HomeController — placeholder only, compiles + responds
  wiring/        AppLoader, AppComponents    the whole object graph, in one place
conf/
  application.conf   loader + mongo config block (pre-wired infra)
  routes             compiled into router.Routes — only GET / here
  logback.xml        console + async file logging, as tested
test/            HomeControllerSpec (stub-based, green)
docker-compose.yml   MongoDB 8 standalone w/ healthcheck
```

- **Compile-time DI.** `play.application.loader = "wiring.AppLoader"` points at `app/wiring/AppLoader.scala`. `AppComponents` extends `BuiltInComponentsFromContext` + `HttpFiltersComponents`; controllers are instantiated by hand there.
- **`conf/routes` is code.** Adding a controller to the routes file changes the generated `Routes` constructor, so you must also instantiate it in `AppComponents` — a missing wire is a compile error, not a runtime surprise.
- **Mongo is pre-wired as infra, not as code.** The driver dependency, the compose file and the `mongo { uri, database }` block in `application.conf` are ready for the first `*RepositoryComponents` trait.

---

## Start a project from this template

1. **Rename** (each appears once): `name` in `build.sbt`, the title/heading here, `container_name` in `docker-compose.yml`, and `mongo.database` in `application.conf`.
2. **Add code** following the pattern from the source project:
   - domain types in `app/model`
   - object-graph slices as traits in `app/services` / `app/repository` — each with `self: BuiltInComponents =>`, all members `lazy val`, config read in the wiring, stop hooks via `applicationLifecycle`
   - add every new slice to the `with` chain of `AppComponents`
   - route + wire controllers in the same step so it compiles
3. **Keep `sbt test` green.**
