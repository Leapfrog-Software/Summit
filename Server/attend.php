<?php

class ChatData {
	public $id;
	public $senderId;
	public $tableId;
	public $datetime;
	public $chat;

	static function initFromFileString($line) {

		$datas = explode(",", $line);

		if (count($datas) == 5) {
			$chatData = new ChatData();
			$chatData->id = $datas[0];
			$chatData->senderId = $datas[1];
			$chatData->tableId = $datas[2];
			$chatData->datetime = $datas[3];
			$chatData->chat = $datas[4];
			return $chatData;
		}
		return null;
	}

	function toFileString() {
		return $this->id . ","
					. $this->senderId . ","
					. $this->tableId . ","
					. $this->datetime . ","
					. $this->chat . "\n";
	}
}

class AttendData {
	public $tableId;
	public $userIds;

	static function initFromFileString($line) {

		$datas = explode(",", $line);
		if (count($datas) == 2) {
			$attendData = new AttendData();
			$attendData->tableId = $datas[0];
			$userIds = explode("-", $datas[1]);
			$attendData->userIds = $userIds;
			return $attendData;
		}
		return null;
	}

	function toFileString() {
		return $this->tableId . ","
					. implode("-", $this->userIds) . "\n";
	}
}

class Attend {

	const DIRECTORY_NAME = "data/attend/";
	const CHAT_FILE_NAME = "chat.txt";
	const ATTEND_FILE_NAME = "attend.txt";

	static function join($scheduleId, $tableId, $userId) {

		Attend::initDirectory($scheduleId);

		$fileName = Attend::DIRECTORY_NAME . $scheduleId . "/" . Attend::ATTEND_FILE_NAME;
		if (file_exists($fileName)) {
			$fileData = file_get_contents($fileName);
			if ($fileData !== false) {
				$attendList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$attendData = AttendData::initFromFileString($lines[$i]);
					if (!is_null($attendData)) {
						$attendList[] = $attendData;
					}
				}
				// 削除
				foreach ($attendList as &$attend) {
					$newUsers = array_diff($attend->userIds, Array($userId));
					$attend->userIds = array_values($newUsers);
				}
				// 追加
				$isFind = false;
				foreach ($attendList as &$attend) {
					if (strcmp($attend->tableId, $tableId) == 0) {
						$attend->userIds[] = $userId;
						$isFind = true;
					}
				}
				if (!$isFind) {
					$newAttendData = new AttendData();
					$newAttendData->tableId = $tableId;
					$newAttendData->userIds[] = $userId;
					$attendList[] = $newAttendData;
				}
				// 保存
				$str = "";
				foreach($attendList as $attend) {
					$str .= $attend->toFileString();
				}
				file_put_contents($fileName, $str);
			}
		}
	}

	static function readChat($scheduleId) {

		$fileName = Attend::DIRECTORY_NAME . $scheduleId . "/" . Attend::CHAT_FILE_NAME;

		if (file_exists($fileName)) {
			$fileData = file_get_contents($fileName);
			if ($fileData !== false) {
				$chatList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$chatData = ChatData::initFromFileString($lines[$i]);
					if (!is_null($chatData)) {
						DebugSave("append");
						$chatList[] = $chatData;
					}
				}
				return $chatList;
			}
		}
		return [];
	}

	static function readAttend($scheduleId) {

		$fileName = Attend::DIRECTORY_NAME . $scheduleId . "/" . Attend::ATTEND_FILE_NAME;

		if (file_exists($fileName)) {
			$fileData = file_get_contents($fileName);
			if ($fileData !== false) {
				$attendList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$attendData = AttendData::initFromFileString($lines[$i]);
					if (!is_null($attendData)) {
						$attendList[] = $attendData;
					}
				}
				return $attendList;
			}
		}
		return [];
	}

	static function postChat($scheduleId, $chatData) {

		Attend::initDirectory($scheduleId);

		$fileName = Attend::DIRECTORY_NAME . $scheduleId . "/" . Attend::CHAT_FILE_NAME;
		file_put_contents($fileName, $chatData->toFileString(), FILE_APPEND);
	}

	static function initDirectory($scheduleId) {

		$directoryName = Attend::DIRECTORY_NAME . $scheduleId;
		if (!file_exists($directoryName)) {
			$mask = umask();
			umask(000);
			mkdir($directoryName, 0777, true);
			umask($mask);
		}
		$chatFileName = Attend::DIRECTORY_NAME . $scheduleId . "/" . Attend::CHAT_FILE_NAME;
		if (!file_exists($chatFileName)) {
			touch($chatFileName);
			$mask = umask();
			umask(000);
			chmod($chatFileName, 0777);
			umask($mask);
		}

		$attendFileName = Attend::DIRECTORY_NAME . $scheduleId . "/" . Attend::ATTEND_FILE_NAME;
		if (!file_exists($attendFileName)) {
			touch($attendFileName);
			$mask = umask();
			umask(000);
			chmod($attendFileName, 0777);
			umask($mask);
		}
	}
}

?>
