package org.iesharia.perdidaslanzarote.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.perdidaslanzarote.model.entities.ItemType
import org.iesharia.perdidaslanzarote.model.entities.Place
import org.iesharia.perdidaslanzarote.viewmodel.AppViewModel

@Composable
fun HomeScreen(appViewModel: AppViewModel) {
    var itemName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<ItemType?>(null) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var expandedPlace by remember { mutableStateOf(false) }

    // Observar los datos desde el ViewModel
    val itemTypes by appViewModel.getItemTypes().collectAsState(initial = emptyList())
    val places by appViewModel.getPlaces().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3E3F1), shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF5A67D8)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registrar una pérdida",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White
                )
            }
        }
        Text(
            text = "¿Qué has perdido?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color(0xFF313131),
            modifier = Modifier.padding(top = 8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemTypes.forEach { type ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable { selectedType = type },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (type == selectedType) Color(0xFF6A5ACD) else Color(0xFFD9DBE9)
                    )
                ) {
                    Text(
                        text = type.name,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                        fontSize = 16.sp,
                        color = if (type == selectedType) Color.White else Color(0xFF4A4A4A)
                    )
                }
            }
        }

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text( text = if (selectedType != null) "Nombre del ${selectedType!!.name.lowercase()}" else "Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFDCDCDC),
                unfocusedContainerColor = Color(0xFFB0B0B0),
                focusedLabelColor = Color(0xFF5A67D8),
                cursorColor = Color(0xFF5A67D8)
            )
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = false,
            maxLines = 3,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFDCDCDC),
                unfocusedContainerColor = Color(0xFFB0B0B0),
                focusedLabelColor = Color(0xFF5A67D8),
                cursorColor = Color(0xFF5A67D8)
            )
        )

        Text(
            text = "¿Dónde lo perdiste?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color(0xFF313131),
            modifier = Modifier.padding(top = 8.dp)
        )
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            OutlinedButton(
                onClick = { expandedPlace = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFD9DBE9),
                    contentColor = Color(0xFF4A4A4A)
                )
            ) {
                Text(selectedPlace?.name ?: "Selecciona un lugar")
            }
            DropdownMenu(
                expanded = expandedPlace,
                onDismissRequest = { expandedPlace = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                places.forEach { place ->
                    DropdownMenuItem(
                        text = { Text(place.name) },
                        onClick = {
                            selectedPlace = place
                            expandedPlace = false
                        }
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF48BB78)),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                if (itemName.isNotBlank() && selectedType != null && selectedPlace != null) {
                    appViewModel.addLostItem(
                        itemName = itemName,
                        itemTypeId = selectedType!!.id,
                        description = description,
                        placeId = selectedPlace!!.id
                    )
                    itemName = ""
                    description = ""
                    selectedType = null
                    selectedPlace = null
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Publicar pérdida",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}
