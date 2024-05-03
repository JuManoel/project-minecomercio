import Persona from "./persona.js";

export default class Cliente extends Persona {
    static colunas=[
        // definir las columnas de la tabla
        { title: 'ID', field: 'id', width: 100, hozAlign: 'center' },
        { title: 'NOMBRE', field: 'nombre', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
        { title: 'CORREO', field: 'correo', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
        { title: 'CREDITO', field: 'credito', hozAlign: 'left' },
        { title: 'TELEFONO.', field: 'telefono', width: 70, hozAlign: 'left', hozAlign: 'center' },
        { formatter: editRowButton, width: 40, hozAlign: 'center', cellClick: super.editRowClick },
        { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: super.deleteRowClick },
      ]
    
      static toComplete(idModal, rowData) {
        super.toComplete()
        if (Persona.currentOption === 'edit') {
          document.querySelector(`${idModal} #credito`).value = rowData.credito
        }
      }
}
  