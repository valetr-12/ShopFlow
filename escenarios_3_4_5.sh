#!/bin/bash
# Regenera los escenarios 3, 4 y 5 del Anexo B con el código corregido.
# Uso: con la app RECIÉN INICIADA, abrir Git Bash en la carpeta del proyecto y ejecutar:
#      bash escenarios_3_4_5.sh
# Sobrescribe evidencias/escenario3.txt, escenario4_1.txt, escenario4_2.txt, escenario5_1.txt y escenario5_2.txt

URL=http://localhost:8080
EV=evidencias
TMP=$(mktemp -d)
mkdir -p "$EV"

if ! curl -s -o /dev/null "$URL/api/pagos/auditoria"; then
  echo "La API no responde en $URL. Arranca ShopflowApplication y vuelve a ejecutar el guion."
  exit 1
fi

# Saca el id del pedido ("PED-n") de una respuesta JSON
id_pedido() { grep -o '"id":"PED-[0-9]*"' "$1" | head -1 | cut -d'"' -f4; }

# ---------------------------------------------------------------- Cuerpos JSON
cat > "$TMP/pedido-co.json" <<'EOF'
{
  "clienteId": "CLI-001",
  "documentoCliente": "1020304050",
  "pais": "CO",
  "contacto": "ana.perez@correo.com",
  "items": [ { "sku": "SKU-001", "cantidad": 1 },
             { "sku": "SKU-002", "cantidad": 2 } ],
  "direccionEnvio": "Calle 134 # 9-51, Bogotá",
  "cupon": "BOSQUE10",
  "envioExpress": true,
  "canalNotificacion": "EMAIL"
}
EOF
cat > "$TMP/sin-items.json" <<'EOF'
{ "clienteId": "CLI-002", "documentoCliente": "1020304050", "pais": "CO",
  "contacto": "ana.perez@correo.com", "items": [],
  "direccionEnvio": "Calle 134 # 9-51, Bogotá", "canalNotificacion": "EMAIL" }
EOF
cat > "$TMP/tienda-express.json" <<'EOF'
{ "clienteId": "CLI-003", "documentoCliente": "1020304050", "pais": "CO",
  "contacto": "ana.perez@correo.com", "items": [ { "sku": "SKU-001", "cantidad": 1 } ],
  "recogerEnTienda": true, "envioExpress": true, "canalNotificacion": "EMAIL" }
EOF
cat > "$TMP/mx-rfc.json" <<'EOF'
{ "clienteId": "CLI-004", "documentoCliente": "GODE561231GR8", "pais": "MX",
  "contacto": "diego.gomez@correo.mx", "items": [ { "sku": "SKU-001", "cantidad": 1 } ],
  "direccionEnvio": "Av. Reforma 222, CDMX", "canalNotificacion": "EMAIL" }
EOF
cat > "$TMP/mx-cedula.json" <<'EOF'
{ "clienteId": "CLI-005", "documentoCliente": "1020304050", "pais": "MX",
  "contacto": "diego.gomez@correo.mx", "items": [ { "sku": "SKU-001", "cantidad": 1 } ],
  "direccionEnvio": "Av. Reforma 222, CDMX", "canalNotificacion": "EMAIL" }
EOF

echo "Escenarios 3, 4 y 5..."
{
  echo "# Escenario 3 - Pedido de Colombia válido (ejemplo B.2)"
  echo "# Esperado: 201, factura DIAN, IVA 19 %, descuento del cupón"
  echo
  curl -s -i -X POST "$URL/api/pedidos" -H "Content-Type: application/json" -d @"$TMP/pedido-co.json"
  echo
} > "$EV/escenario3.txt"

{
  echo "# Escenario 4 - Pedido inválido: sin ítems"
  echo "# Esperado: 400 con mensaje propio"
  echo
  curl -s -i -X POST "$URL/api/pedidos" -H "Content-Type: application/json" -d @"$TMP/sin-items.json"
  echo
} > "$EV/escenario4_1.txt"

{
  echo "# Escenario 4 - Pedido inválido: recoger en tienda con envío express"
  echo "# Esperado: 400 con un mensaje distinto al anterior"
  echo
  curl -s -i -X POST "$URL/api/pedidos" -H "Content-Type: application/json" -d @"$TMP/tienda-express.json"
  echo
} > "$EV/escenario4_2.txt"

{
  echo "# Escenario 5 - Pedido de México con RFC GODE561231GR8"
  echo "# Esperado: 201 con CFDI (SAT) e IVA 16 %"
  echo
  curl -s -i -X POST "$URL/api/pedidos" -H "Content-Type: application/json" -d @"$TMP/mx-rfc.json"
  echo
} > "$EV/escenario5_1.txt"

{
  echo "# Escenario 5 - Pedido de México con la cédula 1020304050"
  echo "# Esperado: 400"
  echo
  curl -s -i -X POST "$URL/api/pedidos" -H "Content-Type: application/json" -d @"$TMP/mx-cedula.json"
  echo
} > "$EV/escenario5_2.txt"

rm -rf "$TMP"
echo "Listo:"
grep -H "HTTP/1.1" "$EV"/escenario3.txt "$EV"/escenario4_*.txt "$EV"/escenario5_*.txt
