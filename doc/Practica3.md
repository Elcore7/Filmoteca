# Problemas encontrados:
> En este Doc se mostrarán algunos problemas o datos curiosos surgidos durante el desarrollo de la práctica 3.

## Doble llamada en "onActivityResult"
> Este caso se ha dado tanto en `FilmEditActivity` y `FilmDataActivity`.

Si se realiza la práctica como se muestra en los enunciados, se acabará llamando 2 veces a la función "onActivityResult". Esto es debido a que si dentro de
la función con *override* "onActivityResult" hacemos la llamada al **super** (super.onActivityResult()) llamará a la definida en la **variable que contenga el contrato**,
el cual a su vez, realizará la llamada "onActivityResult" una vez realizada, teniendo así dos llamadas a "onActivityResult". En mi caso, para aliviar este caso,
he modificado el onActivityResult de la siguiente forma:
``` kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
  when (requestCode) {
              GALLERY_IMAGE_CODE ->
                  if (resultCode == RESULT_OK && data != null && data.data != null) {
                      binding.imageMovie.setImageURI(data.data)
                  }
              CAMERA_IMAGE_CODE ->
                  if (resultCode == RESULT_OK && data != null) {
                          val photo = data?.extras?.get("data") as Bitmap
                          binding.imageMovie.setImageBitmap(photo)
                  }
              else ->
              {
                  if (data != null) {
                      super.onActivityResult(requestCode, resultCode, data)
                  }
              }
          }
}
```
Así me aseguro de que la llamada al **super** solo se ejecute cuando sea extrictamente necesario.

## GET deprecado
> En este caso no he podido encontrar respuestas, las preguntas que tratan el tema son recientes y no se han respondido.

El **GET** usado en `FilmEditActivity` esta marcado como *deprecado* y será eliminado a futuro. Solo se añadieron funciones que tratan los tipos de datos más comunes
pero no existe un **GET** de *Bitmap* no se ha añadido al API 33.
