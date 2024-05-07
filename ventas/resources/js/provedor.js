import Persona from "./persona.js";

export default class Provedor extends Persona{
    static colunas=[
        // definir las columnas de la tabla
        { title: 'ID', field: 'id', width: 100, hozAlign: 'center' },
        { title: 'NOMBRE', field: 'nombre', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
        { title: 'CORREO', field: 'correo', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
        { title: 'INTERMEDIARIO', field: 'intermediario', hozAlign: 'left' },
        { title: 'TELEFONO.', field: 'telefono', width: 70, hozAlign: 'left', hozAlign: 'center' },
        { formatter: editRowButton, width: 40, hozAlign: 'center', cellClick: super.editRowClick },
        { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: super.deleteRowClick },
      ]
      static async init() {
        this.nombreClase='provedor'
        super.init()
      }

      static async addRow() {
        this.nombreClase='provedor'
        Persona.currentOption = 'add'
        Persona.modal = new Modal({
          classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
          title: `<h5>Ingreso de ${this.nombreClase}</h5>`,
          content: Provedor.form,
          buttons: [
            { caption: addButton, classes: 'btn btn-primary me-2', action: () => Provedor.add() },
            { caption: cancelButton, classes: 'btn btn-secondary', action: () => Provedor.modal.close() },
          ],
          doSomething: Provedor.toComplete,
        })
        Provedor.modal.show()
      }
    
      static async add() {
        this.nombreClase='provedor'
        try {
          // verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
          if (!Helpers.okForm(`#form-${this.nombreClase}`)) {
            return
          }
          // obtener del formulario el objeto con los datos que se envían a la solicitud POST
          const body = this.getFormData()
          console.log(body)
          // enviar la solicitud de creación con los datos del formulario
          let response = await Helpers.fetchData(`${urlAPI}/${this.nombreClase}`, {
            method: 'POST',
            body,
          })
          if (response.message === 'ok') {
            this.table.addRow(response.data) // agregar el producto a la tabla
            this.modal.close()
            Toast.show({ message: `${this.nombreClase} agregado exitosamente` })
          } else {
            Toast.show({ message: `No se pudo agregar el ${this.nombreClase}`, mode: 'danger', error: response })
          }
        } catch (e) {
          Toast.show({ message: `Falló la operación de creación del ${this.nombreClase}`, mode: 'danger', error: e })
        }
      }

      static toComplete(idModal, rowData) {
        super.toComplete()
        if (Persona.currentOption === 'edit') {
          document.querySelector(`${idModal} #intermediario`).checked = rowData.intermediario
        }
      }

      static getFormData(){
        const datos=super.getFormData()
        const intermediario= document.querySelector(`#${Persona.modal.id} #intermediario`).checked
        datos.intermediario=intermediario
        return datos
      }
}
  