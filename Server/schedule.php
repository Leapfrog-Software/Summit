<?php

class ScheduleData {
	public $id;
	public $title;
	public $sponsor;
	public $datetime;
	public $interval;
	public $type;

	static function initFromFileString($line) {

		$datas = explode(",", $line);
		if (count($datas) == 7) {
			$scheduleData = new ScheduleData();
			$scheduleData->id = $datas[0];
			$scheduleData->title = $datas[1];
			$scheduleData->sponsor = $datas[2];
			$scheduleData->datetime = $datas[3];
			$scheduleData->interval = $datas[4];
			$scheduleData->type = $datas[5];
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
