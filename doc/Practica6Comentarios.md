# Comentarios:

## Aclaración sobre los archivos fragment y activities
> En mi caso, en vez de modificar los archivos "activity" existentes, he creado nuevos como "fragments".

## Navegación
> Puesto que he ido controlando lo que devolvian los "fragments" y "activities", no he encontrado ninguna falta de implementar la navegación con el NavController 
ni nada de eso. Estoy controlando mejor (de manera específica) el ciclo de vida de los activities y fragments por ello suelo usar "onResume", "onRestart", etc. para
la recarga de datos y vistas.

## OnCreateView
> Esto es más como una nota personal y es el recordar OnViewCreated() ha sido necesario en el fragmento de Data ya que intentando setear antes los valores, hacía
que no encontrase el view aún creado y por ende NO cambiaba los datos al comienzo.
