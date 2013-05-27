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

    public function isPointWithin($latitude, $longitude){
        if($this->radius == null) {
            return false;
        }
        $distance = $this->haversineGreatCircleDistance($this->latitude, $this->longitude, $latitude, $longitude);
        return $distance <= $this->radius;
    }

    /**
     * Calculates the great-circle distance between two points, with
     * the Haversine formula.
     * @param float $latitudeFrom Latitude of start point in [deg decimal]
     * @param float $longitudeFrom Longitude of start point in [deg decimal]
     * @param float $latitudeTo Latitude of target point in [deg decimal]
     * @param float $longitudeTo Longitude of target point in [deg decimal]
     * @param float $earthRadius Mean earth radius in [m]
     * @return float Distance between points in [m] (same as earthRadius)
     */
    function haversineGreatCircleDistance(
      $latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6371000)
    {
        // convert from degrees to radians
        $latFrom = deg2rad($latitudeFrom);
        $lonFrom = deg2rad($longitudeFrom);
        $latTo = deg2rad($latitudeTo);
        $lonTo = deg2rad($longitudeTo);

        $latDelta = $latTo - $latFrom;
        $lonDelta = $lonTo - $lonFrom;

        $angle = 2 * asin(sqrt(pow(sin($latDelta / 2), 2) +
        cos($latFrom) * cos($latTo) * pow(sin($lonDelta / 2), 2)));
        return $angle * $earthRadius;
    }

}

?>
