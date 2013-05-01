<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fabian Castillo <fbn.ecc@gmail.com>
 */
interface APIManager
{
    public function getParams();
    
    public function sendResponse($status = 200, $body = '', $content_type = 'text/html');
    
}

?>
