import React, { useEffect, useState } from 'react';
import style from '../Estilos/Recu.module.css';

const Recu = () => {
    const [formData, setFormData] = useState({
        folio: '',
        email: '',
    });

    useEffect(() => {
        const handleDragOver = (e) => {
            e.preventDefault();
            e.stopPropagation();
        };

        const handleDrop = (e) => {
            e.preventDefault();
            e.stopPropagation();
            const file = e.dataTransfer.files[0];
            if (file) {
                console.log('Archivo arrastrado:', file);
                // Aquí puedes manejar el archivo, como enviarlo a un servidor
            }
        };

        window.addEventListener('dragover', handleDragOver);
        window.addEventListener('drop', handleDrop);

        return () => {
            window.removeEventListener('dragover', handleDragOver);
            window.removeEventListener('drop', handleDrop);
        };
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const enviarDatos = async () => {
        try {
            const response = await fetch('http://localhost:3000/api/recuperarCFDI', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }

            const responseData = await response.json();
            console.log('Respuesta del servidor:', responseData);
            alert('Datos enviados con éxito');
        } catch (error) {
            console.error('Error al enviar los datos:', error);
            alert('Hubo un error al enviar los datos');
        }
    };

    return (
        <div className={style.Elemento}>
            <h1>Recuperación de CFDI</h1>
            <div className={style.folioBusqueda}>

           
            <label htmlFor="folio">Ingrese el número de folio de la factura</label>
            <input
                type="text"
                name="folio"
                placeholder="Folio de ticket"
                className={style.Folio}
                value={formData.folio}
                onChange={handleInputChange}
            />
            <label htmlFor="email">Correo electrónico de la factura</label>
            <input
                type="email"
                name="email"
                placeholder="Correo electrónico"
                className={style.Folio}
                value={formData.email}
                onChange={handleInputChange}
            />
            <button
                type="button"
                className={style.Boton}
                onClick={enviarDatos}
            >
                Enviar
            </button>
            </div>
        </div>
    );
};

export default Recu;