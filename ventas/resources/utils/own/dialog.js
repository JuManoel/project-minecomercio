export default class Dialog {
  id
  instance
  modal

  constructor({ content = 'Sin contenido', modal = true, classes = '' } = {}) {
    this.modal = modal
    // generar un ID de hasta 10 caracteres aleatorios
    this.id = 'dialog-' + Math.random().toString(36).slice(2, 12)

    document.querySelector('body').insertAdjacentHTML(
      'beforeend',
      `<dialog id="${this.id}" class="dialog ${classes}">
      <section id="content-${this.id}"></section>
      </dialog>`
    )

    this.instance = document.querySelector(`#${this.id}`)

    // llamado a los métodos set para asignar los valores del objeto recibido y desestructruado
    this.content = content
  }

  get id() {
    return this.id
  }

  /**
   * Establecer el contenido del cuadro de diálogo
   * @param {string} _content
   */
  set content(_content) {
    if (this.instance) {
      document.querySelector(`#content-${this.id}`).innerHTML = _content
    } else {
      console.error('No se puede asignar contenido a una instancia eliminada del DOM')
    }
  }

  /**
   * @param {string} _style
   */
  set classCSS(_classCSS = '') {
    if (this.instance && _classCSS) {
      this.instance.addClass('classCSS', _classCSS)
    } else {
      console.error('No se pueden asignar estilos a una instancia eliminada del DOM')
    }
  }

  /**
   * bla bla bla
   * @returns bla bla bla
   */
  show() {
    return new Promise((resolve, reject) => {
      if (this.instance) {
        if (this.modal) {
          // el usuario sólo puede interactuar con el cuadro de diálogo abierto
          this.instance.showModal()
        } else {
          // el usuario puede seguir interactuando con otros elementos de la página
          this.instance.show()
        }
        resolve('ok')
      } else {
        reject('No se puede mostrar un dialog removido del DOM')
      }
    })
  }

  close() {
    if (this.instance) {
      this.instance.close()
    } else {
      console.warn('Nada para cerrar. La instancia ya no existe en el DOM')
    }
    return this
  }

  remove() {
    if (this.instance) {
      this.instance.remove()
      // otra forma de eliminar nodos del DOM:
      // this.instance.parentNode.removeChild(this.instance)
    } else {
      console.warn('Nada para eliminar. La instancia ya no existe en el DOM')
    }
    this.instance = undefined
    this.id = undefined
  }
}
