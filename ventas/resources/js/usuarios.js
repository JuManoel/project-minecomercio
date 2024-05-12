export default class Usuarios {
  static #table
  static #modal
  static #currentOption
  static #form
  static #tipoUsuario
  static #modo

  constructor() {
    throw new Error('No utilice el constructor. Use Usuarios.init()')
  }

  static async init(tipo = '') {
    Usuarios.#tipoUsuario = tipo

    // determinar el ID y el label del último campo del formulario, también la última columna que se muestra en la tabla
    Usuarios.#modo = { id: 'credito', label: 'Cliente con crédito', column: { title: 'CRÉDITO', field: 'credito', width: 100, hozAlign: 'center', formatter: 'tickCross' } }
    if (tipo === 'vendedor') {
      Usuarios.#modo = { id: 'admin', label: 'Vendedor con privilegios de administrador', column: { title: 'ADMINISTRADOR', field: 'admin', width: 100, hozAlign: 'center', formatter: 'tickCross' } }
    } else if (tipo == 'provedor') {
      Usuarios.#modo = { id: 'intermediario', label: 'El proveedor es un intermediario', column: { title: 'INTERMEDIARIO', field: 'intermediario', width: 100, hozAlign: 'center', formatter: 'tickCross' } }
    }

    try {
      // adaptar el formulario al tipo de usuario

      // intentar cargar el formulario de edición de usuarios
      Usuarios.#form = await Helpers.loadPage('./resources/html/usuarios.html')
      Usuarios.#form = Usuarios.#form.replace('@id', Usuarios.#modo.id)
      Usuarios.#form = Usuarios.#form.replace('@label', Usuarios.#modo.label)
      Usuarios.#form = Usuarios.#form.replace('@usuario', tipo)

      // intentar cargar los datos de los usuarios
      const response = await Helpers.fetchData(`${urlAPI}/${tipo}`)

      if (response.message !== 'ok') {
        throw new Error(response.message)
      }

      // agregar al <main> de index.html la capa que contendrá la tabla
      document.querySelector('main').innerHTML = `
        <div class="p-2 w-full">
            <div id="table-container" class="m-2"></div>
        </dv>`

      // ver en https://tabulator.info/docs/6.2/columns#definition cómo se definen las propiedades de las columnas
      // ver en https://tabulator.info/docs/6.2/format los diferentes valores de la propiedad formater de las columnas
      Usuarios.#table = new Tabulator('#table-container', {
        height: tableHeight, // establecer la altura de la tabla, esto habilita el DOM virtual y mejora drásticamente la velocidad de procesamiento
        data: response.data, // asignar los datos a la tabla
        layout: 'fitColumns', // ajustar columnas al ancho disponible

        columns: [
          // definir las columnas de la tabla
          { title: 'ID', field: 'id', width: 100, hozAlign: 'center' },
          { title: 'NOMBRE', field: 'nombre', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
          { title: 'CORREO', field: 'correo', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
          { title: 'TELÉFONO', field: 'telefono', width: 130, hozAlign: 'left', hozAlign: 'center' },
          { title: 'CONTRASEÑA', field: 'password', width: 135, visible: false },
          Usuarios.#modo.column,
          { formatter: editRowButton, width: 40, hozAlign: 'center', cellClick: Usuarios.#editRowClick },
          { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: Usuarios.#deleteRowClick },
        ],
        // mostrar al final de la tabla un botón para agregar registros
        footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
      })

      // agregar un gestor de eventos al botón 'add-row' para mostrar el formulario en donde se ingresarán usuarios
      Usuarios.#table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', Usuarios.#addRow))
    } catch (e) {
      Toast.show({ title: 'Ventas', message: e.message, mode: 'danger', error: e })
    }

    return this
  }

  static async #addRow() {
    Usuarios.#currentOption = 'add'
    Usuarios.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Ingreso de ${Usuarios.#tipoUsuario}</h5>`,
      content: Usuarios.#form,
      buttons: [
        { caption: addButton, classes: 'btn btn-primary me-2', action: () => Usuarios.#add() },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Usuarios.#modal.close() },
      ],
      doSomething: Usuarios.#toComplete,
    })
    Usuarios.#modal.show()
  }

  static async #add() {
    try {
      // verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!Helpers.okForm(`#form-usuarios`)) {
        return
      }
      // obtener del formulario el objeto con los datos que se envían a la solicitud POST
      const body = this.#getFormData()
      // enviar la solicitud de creación con los datos del formulario
      let response = await Helpers.fetchData(`${urlAPI}/${this.#tipoUsuario}`, {
        method: 'POST',
        body,
      })
      if (response.message === 'ok') {
        Usuarios.#table.addRow(response.data) // agregar el producto a la tabla
        Usuarios.#modal.close()
        Toast.show({ message: `${this.#tipoUsuario} agregado exitosamente` })
      } else {
        Toast.show({ message: `No se pudo agregar el ${this.#tipoUsuario}`, mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: `Falló la operación de creación del ${this.nombreClase}`, mode: 'danger', error: e })
    }
  }

  static #editRowClick = async (e, cell) => {
    Usuarios.#currentOption = 'edit'
    Usuarios.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Actualización de ${Usuarios.#tipoUsuario}</h5>`,
      content: Usuarios.#form,
      buttons: [
        { caption: editButton, classes: 'btn btn-primary me-2', action: () => Usuarios.#edit(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Usuarios.#modal.close() },
      ],
      doSomething: idModal => Usuarios.#toComplete(idModal, cell.getRow().getData()),
    })
    Usuarios.#modal.show()
  }

  static async #edit(cell) {
    try {
      // verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!Helpers.okForm('#form-usuarios')) {
        return
      }

      // // obtener del formulario el objeto con los datos que se envían a la solicitud PATCH
      const body = Usuarios.#getFormData()

      // configurar la url para enviar la solicitud PATCH
      const url = `${urlAPI}/${this.#tipoUsuario}/${cell.getRow().getData().id}`

      // intentar enviar la solicitud de actualización
      let response = await Helpers.fetchData(url, {
        method: 'PATCH',
        body,
      })

      if (response.message === 'ok') {
        Toast.show({ message: `${this.#tipoUsuario} actualizado exitosamente` })
        cell.getRow().update(response.data)
        Usuarios.#modal.close()
      } else {
        Toast.show({ message: `No se pudo actualizar el ${this.#tipoUsuario}`, mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: `Problemas al actualizar el ${this.#tipoUsuario}`, mode: 'danger', error: e })
    }
  }

  static #deleteRowClick = async (e, cell) => {
    Usuarios.#currentOption = 'delete'
    Usuarios.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Eliminación de ${Usuarios.#tipoUsuario}</h5>`,
      content: `<span class="text-back dark:text-gray-300">
                  Confirme la eliminación del ${Usuarios.#tipoUsuario}:<br>
                  ${cell.getRow().getData().id} – ${cell.getRow().getData().descripcion} – Valor venta $${cell.getRow().getData().valorVenta}<br>
                </span>`,
      buttons: [
        { caption: deleteButton, classes: 'btn btn-primary me-2', action: () => Usuarios.#delete(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Usuarios.#modal.close() },
      ],
    })
    Usuarios.#modal.show()
  }

  static async #delete(cell) {
    try {
      const url = `${urlAPI}/${this.#tipoUsuario}/${cell.getRow().getData().id}`

      // enviar la solicitud de eliminación
      let response = await Helpers.fetchData(url, {
        method: 'DELETE',
      })

      if (response.message === 'ok') {
        Toast.show({ message: `${this.#tipoUsuario} eliminado exitosamente` })
        cell.getRow().delete()
        Usuarios.#modal.close()
      } else {
        Toast.show({ message: `No se pudo eliminar el ${this.#tipoUsuario}`, mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: `Problemas al eliminar el ${this.#tipoUsuario}`, mode: 'danger', error: e })
    }
  }

  static #toComplete(idModal, rowData) {
    if (Usuarios.#currentOption === 'edit') {
      // mostrar los datos de la fila actual en el formulario html
      document.querySelector(`#${idModal} #id`).value = rowData.id
      document.querySelector(`#${idModal} #nombre`).value = rowData.nombre
      document.querySelector(`#${idModal} #correo`).value = rowData.correo
      document.querySelector(`#${idModal} #telefono`).value = rowData.telefono
      //document.querySelector(`#${idModal} #password`).value = rowData.password
      document.querySelector(`#${idModal} #${Usuarios.#modo.id}`).checked = rowData[Usuarios.#modo.id]
    }
  }

  /**
   * Recupera los datos del formulario y crea un objeto para ser retornado
   * @returns Un objeto con los datos del usuario
   */
  static #getFormData() {
    const id = document.querySelector(`#${Usuarios.#modal.id} #id`).value
    const nombre = document.querySelector(`#${Usuarios.#modal.id} #nombre`).value
    const correo = document.querySelector(`#${Usuarios.#modal.id} #correo`).value
    const telefono = document.querySelector(`#${Usuarios.#modal.id} #telefono`).value
    const password = document.querySelector(`#${Usuarios.#modal.id} #password`).value
    const checkbox = document.querySelector(`#${Usuarios.#modal.id} #${Usuarios.#modo.id}`).checked
    const obj = { id, nombre, correo, telefono, password, checkbox }
    obj[Usuarios.#modo.id] = checkbox
    return obj
  }
}
