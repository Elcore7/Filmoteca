# Comentarios:

## Actualizar la lista sin tener un SET específico
> En `ListView`, si se modifica la lista, en vez de hacer que haya una función SET que setee la lista cuando ocurre la recarga de la actividad, se puede llamar a
> `adaptador.notifyDataSetChanged()` (el adapatador del **listView**) para que informe que los datos han cambiado y se recarguen.

## Sobre el intento de toolbar personalizado (Problema)
> Al principio se intentó implementar un toolbar personalizado para reutilizarlo de forma simple, sin embargo tenía problemas a la hora de acerlo trabajar en
> conjunto con el "**Action Mode**" ya que aparecían ambos toolbars (el del toolbar personalizado y el del *action mode*) pese a hacer que el action view modificase
> el menu del toolbar (a la hora de inflarlo), por lo que opté a hacer que el toolbar fuera creado al momento y no tener un diseño preestablecido.

## Evitando el "flasheo" en actvidades lejanas a la principal
> Anteriormente hablé sobre un problema de "flasheo" de pantalla si se usaba el flag `Intent.FLAG_ACTIVITY_CLEAR_TOP` al volver a una intent, pero si se
> usan los "flags": `Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP` se puede realizar la vuelta a la actividad principal sin que la pantalla
> realice el "flasheo".
