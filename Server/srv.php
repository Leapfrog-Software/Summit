<?php

require "user.php";
require "schedule.php";
require "message.php";
require "attend.php";

$command = $_POST["command"];

if (strcmp($command, "getSchedule") == 0) {
	getSchedule();
} else if (strcmp($command, "getUser") == 0) {
	getUser();
} else if (strcmp($command, "getMessage") == 0) {
	getMessage();
} else if (strcmp($command, "postMessage") == 0) {
	postMessage();
} else if (strcmp($command, "createUser") == 0) {
	createUser();
} else if (strcmp($command, "updateUser") == 0) {
	updateUser();
} else if (strcmp($command, "uploadUserImage") == 0) {
	uploadUserImage();
} else if (strcmp($command, "attend") == 0) {
	attend();
} else if (strcmp($command, "postChat") == 0) {
	postChat();
} else {
  echo("unknown");
}

function getSchedule() {

	$data = [];
	$schedules = Schedule::readAll();
	foreach($schedules as $schedule) {
		$data[] = Array(
			"id" => $schedule->id,
			"title" => $schedule->title,
			"sponsor" => $schedule->sponsor,
			"datetime" => $schedule->datetime,
			"type" => $schedule->type,
			"timeLength" => $schedule->timeLength,
			"provider" => $schedule->provider,
			"description" => $schedule->description,
			"filter" => $schedule->filter
		);
	}
	echo(json_encode(Array("result" => "0",
												"data" => $data)));
}

function getUser() {

	$data = [];
	$users = User::readAll();
	foreach($users as $user) {

		$data[] = Array(
			"id" => $user->id,
			"nameLast" => $user->nameLast,
			"nameFirst" => $user->nameFirst,
			"kanaLast" => $user->kanaLast,
			"kanaFirst" => $user->kanaFirst,
			"age" => $user->age,
			"gender" => $user->gender,
			"job" => $user->job,
			"company" => $user->company,
			"position" => $user->position,
			"reserves" => $user->reserves,
			"cards" => $user->cards,
			"message" => $user->message
		);
	}
	echo(json_encode(Array("result" => "0",
												"data" => $data)));
}

function getMessage() {

	$userId = $_POST["userId"];

	$data = [];
	$messages = Message::readMessage($userId);
	foreach ($messages as $message) {
		$data[] = Array(
				"messageId" => $message->messageId,
				"senderId" => $message->senderId,
				"receiverId" => $message->receiverId,
				"message" => $message->message,
				"datetime" => $message->datetime
		);
	}
	echo(json_encode(Array("result" => "0",
												"data" => $data)));
}

function postMessage() {

	date_default_timezone_set('Asia/Tokyo');

	$messageData = new MessageData();
	$messageData->messageId = $_POST["messageId"];
	$messageData->senderId = $_POST["senderId"];
	$messageData->receiverId = $_POST["receiverId"];
	$messageData->datetime = date('YmdHis');
	$messageData->message = $_POST["message"];

	Message::postMessage($messageData);

	echo(json_encode(Array("result" => "0")));
}

function createUser() {

	$result = User::createUser();
	if ($result === false) {
		echo(json_encode(Array("result" => "1")));
	} else {
		$data = Array("userId" => $result);
		$ret = Array("result" => "0", "data" => $data);
		echo(json_encode($ret));
	}
}

function updateUser() {

	$userData = new UserData();
	$userData->id = $_POST["userId"];
	$userData->nameFirst = $_POST["nameFirst"];
	$userData->nameLast = $_POST["nameLast"];
	$userData->kanaFirst = $_POST["kanaFirst"];
	$userData->kanaLast = $_POST["kanaLast"];
	$userData->age = $_POST["age"];
	$userData->gender = $_POST["gender"];
	$userData->job = $_POST["job"];
	$userData->company = $_POST["company"];
	$userData->position = $_POST["position"];
	$userData->reserves = explode("-", $_POST["reserves"]);
	$userData->cards = explode("-", $_POST["cards"]);

	User::updateUser($userData);
	echo(json_encode(Array("result" => "0")));
}

function uploadUserImage() {

	$userId = $_POST["userId"];
	$file = $_FILES['image']['tmp_name'];
	if (User::uploadImage($userId, $file)) {
		echo(json_encode(Array("result" => "0")));
	} else {
		echo(json_encode(Array("result" => "1")));
	}
}

function attend() {

	$userId = $_POST["userId"];
	$scheduleId = $_POST["scheduleId"];
	$tableId = $_POST["tableId"];

	Attend::join($scheduleId, $tableId, $userId);

	$chats = Attend::readChat($scheduleId);
	$chatDatas = [];
	foreach ($chats as $chat) {
		$chatDatas[] = Array(
			"id" => $chat->id,
			"senderId" => $chat->senderId,
			"tableId" => $chat->tableId,
			"datetime" => $chat->datetime,
			"chat" => $chat->chat
		);
	}

	$attends = Attend::readAttend($scheduleId);
	$attendDatas = [];
	foreach ($attends as $attend) {
		$attendDatas[] = Array(
			"tableId" => $attend->tableId,
			"userIds" => $attend->userIds
		);
	}

	echo(json_encode(Array("result" => "0",
	 												"data" => Array(
														"chats" => $chatDatas,
														"attends" => $attendDatas
													)
	)));
}

function postChat() {

	date_default_timezone_set('Asia/Tokyo');

	$scheduleId = $_POST["scheduleId"];

	$chatData = new ChatData();
	$chatData->id = $_POST["chatId"];
	$chatData->senderId = $_POST["senderId"];
	$chatData->tableId = $_POST["tableId"];
	$chatData->datetime = date('YmdHis');
	$chatData->chat = $_POST["chat"];

	Attend::postChat($scheduleId, $chatData);

	echo(json_encode(Array("result" => "0")));
}

function DebugSave($str){

	date_default_timezone_set('Asia/Tokyo');

	$d = date('Y-m-d H:i:s');
	$s = $d . " " . $str . "\r\n";
	file_put_contents("debug.txt", $s, FILE_APPEND);
}

?>
