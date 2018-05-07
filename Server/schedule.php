<?php

class ScheduleData {
	public $id;
	public $title;
	public $provider;
	public $datetime;
	public $timeLength;
	public $type;
	public $description;
	public $filter;

	static function initFromFileString($line) {

		$datas = explode(",", $line);
		if (count($datas) == 8) {
			$scheduleData = new ScheduleData();
			$scheduleData->id = $datas[0];
			$scheduleData->title = $datas[1];
			$scheduleData->provider = $datas[2];
			$scheduleData->datetime = $datas[3];
			$scheduleData->timeLength = $datas[4];
			$scheduleData->type = $datas[5];
			$scheduleData->description = $datas[6];
			$scheduleData->filter = $datas[7];
			return $scheduleData;
		}
		return null;
	}
}

class Schedule {

	const FILE_NAME = "data/schedule.txt";

	static function readAll() {

		if (file_exists(Schedule::FILE_NAME)) {
			$fileData = file_get_contents(Schedule::FILE_NAME);
			if ($fileData !== false) {
				$scheduleList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$scheduleData = ScheduleData::initFromFileString($lines[$i]);
					if (!is_null($scheduleData)) {
						$scheduleList[] = $scheduleData;
					}
				}
				return $scheduleList;
			}
		}
		return [];
	}
}

?>
