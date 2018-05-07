<?php

class UserData {
	public $id;
	public $nameLast;
	public $nameFirst;
	public $kanaLast;
	public $kanaFirst;
	public $age;
	public $gender;
	public $job;
	public $company;
	public $position;
	public $reserves;
	public $cards;
	public $message;

	static function initFromFileString($line) {

		$datas = explode(",", $line);
		if (count($datas) == 13) {
			$userData = new UserData();
			$userData->id = $datas[0];
			$userData->nameLast = $datas[1];
			$userData->nameFirst = $datas[2];
			$userData->kanaLast = $datas[3];
			$userData->kanaFirst = $datas[4];
			$userData->age = $datas[5];
			$userData->gender = $datas[6];
			$userData->job = $datas[7];
			$userData->company = $datas[8];
			$userData->position = $datas[9];
			$userData->reserves = explode("-", $datas[10]);
			$userData->cards = explode("-", $datas[11]);
			$userData->message = $datas[12];
			return $userData;
		}
		return null;
	}

	function toFileString() {

		$reserves = "";
		if (count($this->reserves) > 0) {
			$reserves = implode("-", $this->reserves);
		}

		$cards = "";
		if (count($this->cards) > 0) {
			$cards = implode("-", $this->cards);
		}

		return $this->id . ","
					. $this->nameLast . ","
					. $this->nameFirst . ","
					. $this->kanaLast . ","
					. $this->kanaFirst . ","
					. $this->age . ","
					. $this->gender . ","
		 			. $this->job . ","
					. $this->company . ","
					. $this->position . ","
					. $reserves . ","
					. $cards . "\n";
	}
}

class User {

	const FILE_NAME = "data/user.txt";
	const IMAGE_DIRECTORY = "data/image/user/";

	static function readAll() {

		if (file_exists(User::FILE_NAME)) {
			$fileData = file_get_contents(User::FILE_NAME);
			if ($fileData !== false) {
				$userList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$userData = UserData::initFromFileString($lines[$i]);
					if (!is_null($userData)) {
						$userList[] = $userData;
					}
				}
				return $userList;
			}
		}
		return [];
	}

	static function writeAll($userList) {

		$str = "";
		foreach($userList as $user) {
			$str .= $user->toFileString();
		}
		file_put_contents(User::FILE_NAME, $str);
	}

	static function createUser() {

		$latestUserId = 0;

		$fp = fopen(USER::FILE_NAME, "a+");
		if (flock($fp, LOCK_EX)) {
			fseek($fp, 0);
			while (($buffer = fgets($fp, 1024)) !== false) {
				$exploded = explode(",", $buffer);
				if (count($exploded) >= 2) {
					$userId = (int)$exploded[0];
					if ($userId > $latestUserId) {
						$latestUserId = $userId;
					}
				}
			}
			$userData = new UserData();
			$nextUserId = (string)($latestUserId + 1);
			$userData->id = $nextUserId;

			fputs($fp, $userData->toFileString());
			flock($fp, LOCK_UN);
			fclose($fp);
			return $nextUserId;
		}
		return false;
	}

	static function updateUser($userData) {

		$users = User::readAll();
		foreach ($users as &$user) {
			if (strcmp($user->id, $userData->id) == 0) {
				$user = $userData;
			}
		}
		User::writeAll($users);
	}

	static function uploadImage($userId, $file) {

		$fileName = User::IMAGE_DIRECTORY . $userId;
		return move_uploaded_file($file, $fileName);
	}
}

?>
