# OpenAPI 3.0 Tutorial をベースに開発環境を整える

[OpenAPI 3.0 Tutorial](https://support.smartbear.com/swaggerhub/docs/en/get-started/openapi-3-0-tutorial.html)を触って、OpenAPIによる書き方を勉強します。以降では、チュートリアルサイトに記載されている情報をまとめていく。

## 前書き

### OpenAPIとは何か
* APIの仕様をドキュメント化するための仕様
* 昔はSwagger Specificationと呼ばれていた
* 最新のバージョンはOpenAPI 3.0
* `JSON`か`YAML`で書けるが公式では`YAML`を推奨している

シンプルな OpenAPI 3.0仕様に従うと以下のようになる。
```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Sample API
  description: A sample API to illustrate OpenAPI concepts
paths:
  /list:
    get:
      description: Returns a list of stuff
      responses:
        '200':
          description: Successful response
```

### どこでコードを書くか。
* [SwaggerHub](https://app.swaggerhub.com/home)はWebブラウザだけで全ての作業が完結できるため、触ってみるには簡単でいい。
* [Visual Studio Code](https://code.visualstudio.com/)に[プラグイン](https://marketplace.visualstudio.com/items?itemName=42Crunch.vscode-openapi)を導入して使うのも良い。手元で全ての環境を構築することになるため、個人的にはこちらが推奨。

### 3つのメインセクション
OpenAPI Specificationでは以下の3つのメインセクションがある。各セクションの記載方法についてはチュートリアルを実施しながら説明する。
* Meta information
* Path items (endpoints):
  * Parameters
  * Request bodies
  * Responses
* Reusable components:
  * Schemas (data models)
  * Parameters
  * Responses
  * Other components

## チュートリアルに従って書いてみる。

### 作るAPIの概要
音楽のレコードレーベルのためのAPIを作る。以下の項目を持つアーティストのレコードを有する。
* Artist name
* Artist genre
* Number of albums published under the label
* Artist username

### Meta information
メタ情報には以下のような情報を書く。
* API title
* version
* server URL
* その他の説明

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

# Basic authentication
components:
  securitySchemes
    BasicAuth:
      type: http
      scheme: basic
security:
  - BasicAuth: []

paths: {}
```

#### 認証に関して
認証に関しては[こちら](https://swagger.io/docs/specification/authentication/)を参照。

### Path items
APIのエンドポイントのパス情報を書く。メタ情報で書いたサーバURLからの相対パスで表現する。今回は`/artists`と言うパスを用意するので、クライアントからは`GET https://example.io/v1/artists`と言うアドレスでAPIを呼び出すことになる。

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

# Basic authentication
components:
  securitySchemes
    BasicAuth:
      type: http
      scheme: basic
security:
  - BasicAuth: []

# ---------- Added lines -------------
paths: 
  /artists:
    get:
      description: Returns a list of artists
# ---------- /Added lines ------------
```

### Responses
`GET /artists`エンドポイントを呼び出すと、アーティストのリストを取得する。全てのHTTPレスポンスは少なくとも一つの(クライアントが結果を予想できる)HTTPステータスを返却する必要がある。今回は成功した場合は`200`を、失敗した場合は`400`を返却する。

#### メモ
* [HTTPステータスのリスト](https://www.restapitutorial.com/httpstatuscodes.html)
* [Responsesの書き方の詳細](https://swagger.io/docs/specification/describing-responses/)

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

# Basic authentication
components:
  securitySchemes
    BasicAuth:
      type: http
      scheme: basic
security:
  - BasicAuth: []

paths:
  /artists:
    get:
      description: Returns a list of artists
      # ---------- Added lines -------------
      responses:
        '200':
          description: Successfully returned a list of artists
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  required:
                    - username
                  properties:
                    artist_name:
                      type: string
                    artist_genre:
                      type: string
                    albums_recorded:
                      type: integer
                    username:
                      type: string
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                  properties:
                    message:
                      type: string
        # ---------- /Added lines ------------
```

### Parameters
ユーザーが操作するリソースの可変部分を指定する。

#### メモ
* [パラメータの書き方の詳細](https://swagger.io/docs/specification/describing-parameters/)

#### Query parameters
以下のような取得件数の上限と、取得するページを指定できるクエリパラメータを考える。
```
GET https://example.io/v1/artists?limit=20&offset=3
```
`parameters:`を使って表現する。

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

# Basic authentication
components:
  securitySchemes
    BasicAuth:
      type: http
      scheme: basic
security:
  - BasicAuth: []

paths:
  /artists:
    get:
      description: Returns a list of artists
      # ---------- Added lines -------------
      parameters:
        - name: limit
          in: query
          description: Limits the number of items on a page
          schema:
            type: integer
        - name: offset
          in: query
          description: Specifies the page number of the artist to be displayed
          schema:
            type: integer
      # ---------- /Added lines ------------
      responses:
        '200':
          description: Successfully returned a list of artists
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  required:
                    - username
                  properties:
                    artist_name:
                      type: string
                    artist_genre:
                      type: string
                    albums_recorded:
                      type: integer
                    username:
                      type: string
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                  properties:
                    message:
                      type: string
```

### Request body
`POST`, `PUT`, 'PATCH'メソッドのリクエストではリクエストボディが含まれる。リクエストボディは`requestBody`オブジェクトを使って表現する。`/artists`パスに`POST`リクエストを追加してみる。

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

# Basic authentication
components:
  securitySchemes
    BasicAuth:
      type: http
      scheme: basic
security:
  - BasicAuth: []

paths:
  /artists:
    get:
      description: Returns a list of artists
      parameters:
        - name: limit
          in: query
          description: Limits the number of items on a page
          schema:
            type: integer
        - name: offset
          in: query
          description: Specifies the page number of the artist to be displayed
          schema:
            type: integer
      responses:
        '200':
          description: Successfully returned a list of artists
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  required:
                    - username
                  properties:
                    artist_name:
                      type: string
                    artist_genre:
                      type: string
                    albums_recorded:
                      type: integer
                    username:
                      type: string
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
    # ---------- Added lines ------------
    post:
      description: Lets a user post a new artist
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required:
                - usename
              properties:
                artist_name:
                  type: string
                artist_genre:
                  type: string
                albums_recorded:
                  type: integer
                username:
                  type: string
      responses:
        '200':
          description: Successfully created a new artist
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
    # ---------- /Added lines ------------
```

### Path parameters
以下のようなアーティスト名を指定できるようなパスを考える。
```
https://example.io/v1/artists/{username}
```
このURLに対応する別のパスを追加する。
```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

# Basic authentication
components:
  securitySchemes
    BasicAuth:
      type: http
      scheme: basic
security:
  - BasicAuth: []

paths:
  /artists:
    get:
      description: Returns a list of artists
      parameters:
        - name: limit
          in: query
          description: Limits the number of items on a page
          schema:
            type: integer
        - name: offset
          in: query
          description: Specifies the page number of the artist to be displayed
          schema:
            type: integer
      responses:
        '200':
          description: Successfully returned a list of artists
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  required:
                    - username
                  properties:
                    artist_name:
                      type: string
                    artist_genre:
                      type: string
                    albums_recorded:
                      type: integer
                    username:
                      type: string
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
    post:
      description: Lets a user post a new artist
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required:
                - usename
              properties:
                artist_name:
                  type: string
                artist_genre:
                  type: string
                albums_recorded:
                  type: integer
                username:
                  type: string
      responses:
        '200':
          description: Successfully created a new artist
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
  # ---------- Added lines ------------
  /artists/{username}:
    get:
      description: Obtain infomation about an artist from his or her unique username
      parameters:
        - name: username
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Successfully returned an artist
          content:
            application/json:
              schema:
                type: object
                properties:
                  artist_name:
                    type: string
                  artist_genre:
                    type: string
                  albums_recorded:
                    type: integer
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
  # ---------- /Added lines ------------
```
ここまでで、ひとまずAPIの設計は完了！おめでとうございます！

### Reusable components
ここまでで2つのエンドポイントと3つのアクションを設計した。同じレスポンスやスキーマについては `components`セクションにまとめよう。再利用なコンポーネントの種類は以下がある。
* Schemas (data models)
* Parameters
* Request bodies
* Responses
* Response headers
* Examples
* Links
* Callbacks

### Schemas
`HTTP 200`を返却する場合のレスポンスのスキーマを`components`にまとめてみよう。`securitySchemes`を定義するためにすでに`components`セクションは用意していたので、このタイミングで`components`セクションは末尾に移動する。

#### メモ
* [コンポーネントについての説明](https://swagger.io/docs/specification/components/)

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

security:
  - BasicAuth: []

paths:
  /artists:
    get:
      description: Returns a list of artists
      parameters:
        - name: limit
          in: query
          description: Limits the number of items on a page
          schema:
            type: integer
        - name: offset
          in: query
          description: Specifies the page number of the artist to be displayed
          schema:
            type: integer
      responses:
        '200':
          description: Successfully returned a list of artists
          content:
            application/json:
              schema:
                type: array
                items:
                  # ------------- Added Line -----------------
                  $ref: '#/components/schemas/Artist'
                  # ------------- /Added Line ----------------
'400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
    post:
      description: Lets a user post a new artist
      requestBody:
        required: true
        content:
          application/json:
            schema:
              # ------------- Added Line -----------------
              $ref: '#/components/schemas/Artist'
              # ------------- /Added Line ----------------
      responses:
        '200':
          description: Successfully created a new artist
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
  /artists/{username}:
    get:
      description: Obtain infomation about an artist from his or her unique username
      parameters:
        - name: username
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Successfully returned an artist
          content:
            application/json:
              schema:
                type: object
                properties:
                  artist_name:
                    type: string
                  artist_genre:
                    type: string
                  albums_recorded:
                    type: integer
        '400':
          description: Invalid request
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string

components:
  securitySchemes:
    BasicAuth:
      type: http
      scheme: basic

  # ---------- Added lines ------------
  schemas:
    Artist:
      type: object
      required:
        - username
      properties:
        artist_name:
          type: string
        artist_genre:
          type: string
        albums_recorded:
          type: integer
        username:
          type: string
  # ---------- /Added lines -----------
```

### Parameters and Responses
クエリパラメータ`offset`と`limit`と、`400Error`レスポンスについてもコンポーネント化してみる。

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

security:
  - BasicAuth: []

paths:
  /artists:
    get:
      description: Returns a list of artists
      parameters:
        - name: limit
          in: query
          description: Limits the number of items on a page
          schema:
            type: integer
        - name: offset
          in: query
          description: Specifies the page number of the artist to be displayed
          schema:
            type: integer
      responses:
        '200':
          description: Successfully returned a list of artists
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Artist'
        '400':
          # --------------- Added lines -------------------
          $ref: '#/components/responses/400Error'
          # --------------- /Added lines ------------------
    post:
      description: Lets a user post a new artist
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Artist'
      responses:
        '200':
          description: Successfully created a new artist
        '400':
          # --------------- Added lines -------------------
          $ref: '#/components/responses/400Error'
          # --------------- /Added lines ------------------
  /artists/{username}:
    get:
      description: Obtain infomation about an artist from his or her unique username
      parameters:
        - name: username
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Successfully returned an artist
          content:
            application/json:
              schema:
                type: object
                properties:
                  artist_name:
                    type: string
                  artist_genre:
                    type: string
                  albums_recorded:
                    type: integer
        '400':
          # --------------- Added lines -------------------
          $ref: '#/components/responses/400Error'
          # --------------- /Added lines ------------------
components:
  securitySchemes:
    BasicAuth:
      type: http
      scheme: basic

  schemas:
    Artist:
      type: object
      required:
        - username
      properties:
        artist_name:
          type: string
        artist_genre:
          type: string
        albums_recorded:
          type: integer
        username:
          type: string

  # --------------- Added lines -------------------
  parameters:
    PageLimit:
      name: limit
      in: query
      description: Limits the number of items on a page
      schema:
        type: integer

    PageOffset:
      name: offset
      in: query
      description: Specifies the page number of the artists to be displayed
      schema:
        type: integer

  responses:
    400Error:
      description: Invalid request
      content:
        application/json:
          schema:
            type: object
            properties:
              message:
                type: string
  # --------------- /Added lines ------------------
```

### サマリー
ここまでで基本は完了。基本的なRESTful APIの設計ができるようになった。ここから先は以下の公式ドキュメントを都度読んで勉強しよう。[SmartBear Academy](https://smartbear.com/academy/)にも色々な教材がある。
* [OpenAPI Specification](https://swagger.io/specification/)
* [Basic Structure](https://swagger.io/docs/specification/basic-structure/)

## Prismを利用したモックサーバを構築する
[Prism](https://stoplight.io/open-source/prism)はオープンソースのHTTPモックサーバ。

### インストール・セットアップ
以下のコマンドでPrismをインストールする。
```shell
npm install --save-dev @stoplight/prism-cli
```
次に`package.json`に実行のためのコマンドを追加する。
```json
  "scripts": {
    "run:mockserver": "prism mock ./tutorial.yaml"
  },
```
以下のコマンドを実行すると、OpenAPI定義に従ったMockサーバが起動する。
```shell
npm run run:mockserver
```
この状態で以下のコマンドを実行するとレスポンスが正しく取得できる。なお、以下コマンドで[httpie](https://httpie.io)を利用。
```shell
# GET /artists
http -a user:pass http://localhost:4010/artists

# GET /artists/1
http -a user:pass http://localhost:4010/artists/1

# POST /artists
http -a user:pass POST http://localhost:4010/artists artist_name=kuzukawa artist_genre=rock albums_recorded:=5 username=kuzukawa
```

### テストデータを改善する
今の状態だと以下のような適当な値が返却されてしまう。
```shell
❯ http -a user:pass http://localhost:4010/artists
HTTP/1.1 200 OK
Access-Control-Allow-Credentials: true
Access-Control-Allow-Headers: *
Access-Control-Allow-Origin: *
Access-Control-Expose-Headers: *
Connection: keep-alive
Content-Length: 90
Content-type: application/json
Date: Thu, 01 Feb 2024 11:58:52 GMT
Keep-Alive: timeout=5

[
    {
        "albums_recorded": 0,
        "artist_genre": "string",
        "artist_name": "string",
        "username": "string"
    }
]
```

Mockサーバ経由であっても適切な値を返却するよう、OpenAPI定義を修正する。`example`を利用することによりテストデータの値を定義することができる。

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: Simple Artist API
  description: A simple API to illustrate OpenAPIとは何か

servers:
  - url: https://example.io/v1

security:
  - BasicAuth: []

paths:
  /artists:
    get:
      description: Returns a list of artists
      parameters:
        - name: limit
          in: query
          description: Limits the number of items on a page
          schema:
            type: integer
        - name: offset
          in: query
          description: Specifies the page number of the artist to be displayed
          schema:
            type: integer
      responses:
        '200':
          description: Successfully returned a list of artists
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Artist'
        '400':
          $ref: '#/components/responses/400Error'
    post:
      description: Lets a user post a new artist
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Artist'
      responses:
        '200':
          description: Successfully created a new artist
        '400':
          $ref: '#/components/responses/400Error'
  /artists/{username}:
    get:
      description: Obtain infomation about an artist from his or her unique username
      parameters:
        - name: username
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Successfully returned an artist
          content:
            application/json:
              schema:
                type: object
                properties:
                  artist_name:
                    type: string
                  artist_genre:
                    type: string
                  albums_recorded:
                    type: integer
        '400':
          $ref: '#/components/responses/400Error'
components:
  securitySchemes:
    BasicAuth:
      type: http
      scheme: basic

  schemas:
    Artist:
      type: object
      required:
        - username
      properties:
        artist_name:
          type: string
        artist_genre:
          type: string
        albums_recorded:
          type: integer
        username:
          type: string
      # --------------- Added lines -------------------
      example:
        artist_name: 'kuzukawa'
        artist_genre: 'rock'
        albums_recorded: 5
        username: 'kuzukawa'
      # --------------- /Added lines ------------------

  parameters:
    PageLimit:
      name: limit
      in: query
      description: Limits the number of items on a page
      schema:
        type: integer

    PageOffset:
      name: offset
      in: query
      description: Specifies the page number of the artists to be displayed
      schema:
        type: integer

  responses:
    400Error:
      description: Invalid request
      content:
        application/json:
          schema:
            type: object
            properties:
              message:
                type: string
```