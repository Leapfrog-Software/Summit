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

class Chat {

	const DIRECTORY_NAME = "data/chat/";
	const CHAT_FILE_NAME = "chat.txt";
	const ATTEND_FILE_NAME = "attend.txt";

	static function attend($scheduleId, $tableId, $userId) {

		Chat::createDirectory($scheduleId);

		$fileName = Chat::DIRECTORY_NAME . $scheduleId . "/" . Chat::ATTEND_FILE_NAME;
		if (file_exists($fileName)) {
			$fileData = file_get_contents($fileName);
			if ($fileData !== false) {
				$attendList = [];
				$lines = explode("\n", $attendList);
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
				foreach ($attendList as &$attend) {
					if (strcmp($attend->tableId, $tableId) == 0) {
						$attend->userIds[] = $userId;
					}
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

		$fileName = Chat::DIRECTORY_NAME . $scheduleId . "/" . Chat::CHAT_FILE_NAME;

		if (file_exists($fileName)) {
			$fileData = file_get_contents($fileName);
			if ($fileData !== false) {
				$chatList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$chatData = ChatData::initFromFileString($lines[$i]);
					if (!is_null($chatData)) {
						$chatList[] = $chatData;
					}
				}
				return $chatList;
			}
		}
		return [];
	}

	static function readAttend($scheduleId) {

		$fileName = Chat::DIRECTORY_NAME . $scheduleId . "/" . Chat::ATTEND_FILE_NAME;

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

		Chat::createDirectory($scheduleId);

		$fileName = Chat::DIRECTORY_NAME . $scheduleId . "/" . Chat::CHAT_FILE_NAME;
		file_put_contents($fileName, $chatData->toFileString(), FILE_APPEND);
	}

	static function createDirectory($scheduleId) {

		$directoryName = Chat::DIRECTORY_NAME . $scheduleId;
		if (!file_exists($directoryName)) {
			mkdir($directoryName, 0777);
		}
	}
}

?>
