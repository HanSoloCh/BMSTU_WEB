#!/bin/bash
# ==============================
# Нагрузочное тестирование и подсчёт распределения запросов по backend
# ==============================

BASE_URL="http://localhost"
GET_ENDPOINT="/api/v1/books"
POST_ENDPOINT="/api/v1/books"
POST_DATA="post_data.json"

# --- Проверка POST файла ---
if [ ! -f "$POST_DATA" ]; then
  echo "Файл $POST_DATA не найден! Создай JSON с телом POST."
  exit 1
fi

# --- 1. GET нагрузка ---
echo "Запуск GET нагрузки 1000 запросов с 50 параллельными..."
ab -n 1000 -c 50 "$BASE_URL$GET_ENDPOINT" > report_get.txt
echo "Отчёт GET сохранён в report_get.txt"

# --- 2. POST нагрузка ---
echo "Запуск POST нагрузки 100 запросов с 10 параллельными..."
ab -n 100 -c 10 -p "$POST_DATA" -T application/json "$BASE_URL$POST_ENDPOINT" > report_post.txt
echo "Отчёт POST сохранён в report_post.txt"

# --- 3. Подсчёт GET запросов по backend ---
echo ""
echo "Распределение GET-запросов по backend:"
docker compose logs nginx 2>&1 \
  | grep "\[GET $GET_ENDPOINT\]" \
  | awk '{print $5}' | sort | uniq -c

# --- 4. Подсчёт POST запросов по backend ---
echo ""
echo "Распределение POST-запросов по backend:"
docker compose logs nginx 2>&1 \
  | grep "\[POST $POST_ENDPOINT\]" \
  | awk '{print $5}' | sort | uniq -c

echo ""
echo "✅ Тестирование и подсчёт завершены."
