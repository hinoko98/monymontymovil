# MonyMontyMovil

App móvil (Flutter) de MonyMonty. Reutiliza la misma API que la web
([MonyMontyApi](../MonyMontyApi)): login por sesión con cookie, igual que
hace [MonyMonty](../MonyMonty) (`axios` con `withCredentials: true`).

## Cómo funciona el login

- `POST auth/login` — email/password, la API responde `{ token }` y deja una
  cookie de sesión (`express-session`).
- `GET auth/check` — valida si la cookie guardada sigue siendo una sesión activa.
- `GET user/me` — perfil del usuario autenticado.
- `GET auth/logout` — cierra sesión en el servidor.

El cliente HTTP (`lib/core/api_client.dart`) usa `dio` + `cookie_jar`
persistido en disco para comportarse igual que un navegador: guarda la
cookie que llega en el login y la reenvía en cada petición siguiente, y la
conserva entre reinicios de la app (sesión recordada).

## Apuntar a la API

Por defecto (`lib/core/env.dart`):

- Emulador Android → `http://10.0.2.2:3000/` (así se ve `localhost` del host).
- iOS Simulator → `http://localhost:3000/`.

Para un dispositivo físico o un backend distinto, sobreescribe con:

```bash
flutter run --dart-define=API_BASE_URL=http://192.168.1.10:3000/
```

y corre la API con `EXPRESS_HOST=0.0.0.0` (en su `.env`) para que acepte
conexiones desde la red local. Además agrega esa IP a `CORS_ORIGIN` si vas a
seguir probando la web desde el mismo backend.

> El tráfico HTTP (sin TLS) solo está permitido hacia `localhost`/`10.0.2.2`
> (ver `network_security_config.xml` en Android e `Info.plist` en iOS). En
> producción, la API se sirve por HTTPS y no hace falta ninguna excepción.
