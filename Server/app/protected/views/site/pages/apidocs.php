<?php
/* @var $this SiteController */

$this->pageTitle = Yii::app()->name . ' - API Docs';
$this->breadcrumbs = array(
    'About',
);

Yii::app()->clientScript->registerScriptFile("https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js");
?>
<div class="row">
    <div class="span-4" style="width:300px">
        <ul class="nav nav-list">
            <li class="nav-header"><a href="#estudiantes">Estudiantes</a></li>
            <li><a href="#reg_estudiante">Registrar estudiante</a></li>
            <li class="nav-header"><a href="#ejercicios">Ejercicios</a></li>
            <li><a href="#obtener_ejercicio">Obtener un ejercicio</a></li>
            <li><a href="#enviar_ejercicio">Enviar respuesta a un ejercicio</a></li>
            <li class="divider"></li>
            <li><a href="#general_errors">Errores generales</a></li>
        </ul>
    </div>

    <div class="span-8" style="width:800px">
        <h1>API</h1>

        <p>Toda la funcionalidad del API se encuentra bajo la ruta /api. 
            <code>http://mobileservice.site40.net/api</code></p>

        <h2 id="estudiantes">Estudiantes</h2>
        <div class="method">
            <h3 id="reg_estudiante">Registrar un estudiante</h3>
            <p>Registra a un nuevo estudiante.</p>
            <pre class="prettyprint">POST /estudiante</pre>

            <h4>Input</h4>
            <dl>
                <dt>matricula</dt>
                <dd>string</dd>
                <dt>nombre</dt>
                <dd>string</dd>
                <dt>licenciatura</dt>
                <dd>string</dd>
            </dl>
            <pre class="prettyprint">
{
    "matricula": "09200400",
    "nombre": "Juan Pérez",
    "licenciatura": "LIS"
}
            </pre>

            <h4>Response</h4>
            <pre class="prettyprint">
HEADER
    Status: 200 OK
            </pre>

            <h5>Errores</h5>
            <pre class="prettyprint">
400:
    Parametros faltantes
409:
    Ya existe estudiante con el id especificado
            </pre>

        </div>

        <h2 id="ejercicios">Ejercicios</h2>
        <div class="method">
            <h3 id="obtener_ejercicio">Obtener un ejercicio</h3>
            <p>Obtiene la información de un ejercicio.</p>
            <pre class="prettyprint">GET /ejercicio/&lt;id del ejercicio&gt;</pre>

            <h4>Response</h4>

            <h5>Object</h5>
            <dl>
                <dt>id</dt>
                <dd>string</dd>
                <dt>titulo</dt>
                <dd>string</dd>
                <dt>descripcion</dt>
                <dd>string</dd>
                <dt>sentencias</dt>
                <dd>array of strings</dd>
            </dl>

            <pre class="prettyprint">
<strong>HEADER</strong>
    Status: 200 OK

<strong>CONTENT:</strong>
{
    "id": "UnIdentificador",
    "titulo": "Titulo del ejercicio",
    "descripcion": "Descripción del ejercicio",
    "sentencias": ["sentencia 1", "sentencia2", "sentencia3" ]
}
            </pre>

            <h5>Errores</h5>
            <pre class="prettyprint">
404: No existe un ejercicio con el id especificado
            </pre>

        </div>
        <div class="method">
            <h3 id="enviar_ejercicio">Enviar respuesta a un ejercicio</h3>
            <p>Registra la respuesta a un ejercicio. Se requieren también los datos
            del estudiante. En base a la matrícula, que actúa como identificación,
            se registra un nuevo estudiante o de ser necesario se actualizan sus datos.
            </p>
            <pre class="prettyprint">POST /ejercicio</pre>
            <h4>Input</h4>
            <dl>
                <dt>matricula</dt>
                <dd>string (del estudiante)</dd>
                <dt>nombre</dt>
                <dd>string (del estudiante)</dd>
                <dt>licenciatura</dt>
                <dd>string (del estudiante)</dd>
                <dt>idEjercicio</dt>
                <dd>string</dd>
                <dt>duracion (en minutos)</dt>
                <dd>integer</dd>
                <dt>respuestas</dt>
                <dd>array of integers</dd>
                <dt>comentarios</dt>
                <dd>(opcional) string</dd>
            </dl>
            <pre class="prettyprint">
{
    "matricula": "09200400",
    "nombre": "Juan Pérez",
    "licenciatura": "LIS"
    "idEjercicio": "ej1",
    "idUsuario": "09200400",
    "duracion": 15,
    "respuestas": [1,3,2,4],
    "comentarios": "Me pareció difícil"
}
            </pre>

            <h4>Response</h4>

            <h5>Object</h5>
            <dl>
                <dt>esCorrecto</dt>
                <dd>boolean</dd>
                <dt>mensaje</dt>
                <dd>string</dd>
            </dl>

            <pre class="prettyprint">
<strong>HEADER</strong>
    Status: 200 OK

<strong>CONTENT:</strong>
{
    "esCorrecto: true,
    "mensaje": "Ejercicio realizado con éxito"
}
            </pre>

            <h5>Errores</h5>
            <pre class="prettyprint">
400: (Errores en la petición)
    Parámetros faltantes
    Número de respuestas es diferente al número de preguntas del ejercicio
    No existe estudiante con el id especificado
404:
    No existe ejercicio con el id especificado
409:
     El estudiante especificado ya ha resuelto el ejercicio
            </pre>


        </div>


        <h3 id="errores">Errores generales</h3>
        <pre class="prettyprint">
400:
    Errores en la petición (sintaxis o incompletez)
500:
    Error interno del servidor
        </pre>

    </div>

</div>