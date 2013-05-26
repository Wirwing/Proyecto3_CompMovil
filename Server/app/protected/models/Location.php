<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Represents a location.
 * It's a circle over an area.
 * @property double $latitude
 * @property double $longitude
 * @property double $radius (in meters)
 *
 * @author Fabian Castillo <fbn.ecc@gmail.com>
 */
class Location
{

    public $latitude;
    public $longitude;
    public $radius;

    public function __construct($latitude, $longitude, $radius = null)
    {
        $this->latitude = $latitude;
        $this->longitude = $longitude;
        $this->radius = $radius;
    }

    public function getString()
    {
        return implode(',', $this->getArray());
    }
    
    public function getArray(){
        $array = array($this->latitude, $this->longitude);
        if($this->radius != null){
            $array[] = $this->radius;
        }
        return $array;
    }

}

?>
