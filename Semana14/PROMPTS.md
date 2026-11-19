# PROMPTS.md

Registro de los prompts importantes usados con Gemini en la Fase 2 (rama mejora-ia-semana14).

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un tiempo máximo con withTimeout a cargarUnaPorUna y cargarALaVez; si se pasa, mostrar "Tardó demasiado". Atrapa TimeoutCancellationException antes de relanzar CancellationException. Prohibido GlobalScope, runBlocking y Thread.sleep.» | Envolvió cada carga con withTimeout y agregó un catch para el timeout que publica CargaUi.Error("Tardó demasiado"). | Ajusté el límite a 4000 ms en la constante TIEMPO_MAXIMO_MS y puse el catch del timeout antes del catch de CancellationException. | Con 2 s las dos cargas fallarían siempre (tardan 2 s y 3 s). Con 4 s A la vez pasa y Una por una falla. El orden de los catch evita que el timeout se confunda con una cancelación normal. |
