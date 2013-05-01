<?php

Yii::import('application.api.*');

class ApiController extends Controller implements APIManager
{

    Const APPLICATION_ID = 'ASCCPE';
    Const HEADER_JSON = 'application/json';

    private $_params = array();

    const MSG_MISSING_PARAMS = 'Your request is missing parameters.';

    private $behaviors = array(
        'exercise' => 'Exercise',
        'ejercicio' => 'Exercise',
        'ejercicios' => 'Exercise',
        'estudiante' => 'Student',
        'estudiantes' => 'Student',
        'usuario' => 'Student',
    );

    public function filters()
    {
        return array();
    }

    public function actionList()
    {

        $model = $_GET['model'];

        if (isset($this->behaviors[$model])) {
            $class = $this->behaviors[$model];
            $object = $this->_loadAPIModel($class);
            if ($object) {
                $object->actionList($this);
            } else {
                $this->sendResponse(500);
            }
        } else {
            $this->sendResponse(501);
        }
    }

    public function actionView()
    {
        $model = $_GET['model'];
        if (!isset($this->behaviors[$model])) {
            $this->sendResponse(501);
        }

        $idModel = null;
        if (isset($_GET['id'])) {
            $idModel = $_GET['id'];
        } else {
            $this->sendResponse(500, 'Error: Parameter <b>id</b> is missing');
            Yii::app()->end();
        }

        $class = $this->behaviors[$model];
        $object = $this->_loadAPIModel($class);
        if ($object) {
            $object->actionView($idModel, $this);
        } else {
            $this->sendResponse(500);
        }
    }

    public function actionCreate()
    {
        $this->_parseIncomingParams();
        $model = $_GET['model'];
        if (!isset($this->behaviors[$model])) {
            $this->sendResponse(501);
        }

        $class = $this->behaviors[$model];
        $object = $this->_loadAPIModel($class);
        if ($object) {
            $object->actionCreate($this);
        } else {
            $this->sendResponse(500);
        }
    }

    public function actionUpdate()
    {
        $this->sendResponse(501);
    }

    public function actionDelete()
    {
        $this->sendResponse(501);
    }

    /**
     * Originally taken from http://www.gen-x-design.com/archives/create-a-rest-api-with-php
     */
    public function sendResponse($status = 200, $body = '', $content_type = 'text/html')
    {
        // set the status
        $status_header = 'HTTP/1.1 ' . $status . ' ' . $this->_getStatusCodeMessage($status);
        header($status_header);
        // and the content type
        header('Content-type: ' . $content_type);

        if ($body != '') {
            echo $body;
        }
        // we need to create the body if none is passed
        else {
            // create some body messages
            $message = '';

            // this is purely optional, but makes the pages a little nicer to read
            // for your users.  Since you won't likely send a lot of different status codes,
            // this also shouldn't be too ponderous to maintain
            switch ($status) {
                case 401:
                    $message = 'You must be authorized to view this page.';
                    break;
                case 404:
                    $message = 'The requested URL ' . $_SERVER['REQUEST_URI'] . ' was not found.';
                    break;
                case 409:
                    $message = 'Request could not be processed because of conflict in the request';
                    break;
                case 500:
                    $message = 'The server encountered an error processing your request.';
                    break;
                case 501:
                    $message = 'The requested method is not implemented.';
                    break;
            }

            // servers don't always have a signature turned on 
            // (this is an apache directive "ServerSignature On")
            $signature = ($_SERVER['SERVER_SIGNATURE'] == '') ? $_SERVER['SERVER_SOFTWARE'] . ' Server at ' . $_SERVER['SERVER_NAME'] . ' Port ' . $_SERVER['SERVER_PORT'] : $_SERVER['SERVER_SIGNATURE'];
            echo $body;
        }
        Yii::app()->end();
    }

    public function getParams()
    {
        return $this->_params;
    }

    private function _getStatusCodeMessage($status)
    {
        $codes = Array(
            200 => 'OK',
            400 => 'Bad Request',
            401 => 'Unauthorized',
            402 => 'Payment Required',
            403 => 'Forbidden',
            404 => 'Not Found',
            500 => 'Internal Server Error',
            501 => 'Not Implemented',
        );
        return (isset($codes[$status])) ? $codes[$status] : '';
    }

    private function _parseIncomingParams()
    {
        $body = file_get_contents("php://input");
        $body_params = CJSON::decode($body);
        if ($body_params) {
            $this->_params = $body_params;
        }
    }

    private function _loadAPIModel($model)
    {
        $class = $this->_constructClassName($model);
        $object = new $class();
        return $object;
    }

    private function _constructClassName($model)
    {
        return ucfirst($model) . 'API';
    }

}