<?php
/**
* Utility functions related to arrays.
*/
class ArrayUtilities {

	/**
	 * Obtains a string representation of a simple arrray.
	 * It "cleans" the array so empty or null values become the empty
	 * string "". Also number values lower than 0 become the empty string.
	 * Examples: array(1,null,3) => "1,,3"
	 *			 array(3,5,-1,4) => "3,5,,4"
	 * @param $array The array to transform
	 * @param $glue (optional) The separator of the elements in the string
	 * representation. Defaults to: ','.
	 */
	public static function arrayToString($array, $glue = ','){
		$array = self::_cleanSimpleArrayForString($array);
		return implode($glue, $array);
	}

	// Replace the empty and lower than 0 values in the strings
	// for the empty string ("");
	private static function _cleanSimpleArrayForString($array){
		for($i = 0; $i < count($array); $i++){
			if($array[$i] === null || $array[$i] < 0){
				$array[$i] = '';
			}
		}
                return $array;
	}
}