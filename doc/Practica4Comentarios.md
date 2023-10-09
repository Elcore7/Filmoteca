# Casos encontrados:

## Solo pasar un Intent con el objeto al completo
> Si se busca pasar un item al completo (Por ejemplo un "Film"), se puede hacer que dicha clase extienda de Serializable. Después cuando se quiera
> añadir el **extra**, se hará cast en el *putExtra* ->  `(Serializable) film` y después se recuperará con `getSerializableExtra()`.

## ListView deprecado
> Si se intenta hacer que una Activity (en mi caso *FilmListActivity*) extienda de `ListView`, además de que ya está deprecado, se puede seguir usando el
> componente `ListView` del XML perfectamente con **AppCompatActivity()**. También como sustituto, se recomienda usar `ListFragment` pero al no tener problemas con la implementación actual, no se ha probado este caso.

## Problemas con el guardado de la imagen
> Se ha conseguido guardar las imágenes en el dispoisitivo pero han ocurrido problemas con la obtención del bitmap de dichas imágenes.
> Se ha optado por simplemente añadir la propiedad Bitmap a la clase **Film** y modificar dicha propiedad para cambiar las imágenes



