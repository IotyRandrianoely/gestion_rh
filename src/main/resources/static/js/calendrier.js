document.addEventListener('DOMContentLoaded', function() {
  const calendarEl = document.getElementById('calendar');
  const eventMenu = document.getElementById('event-menu');
  const cancelButton = document.getElementById('cancel-interview');
  let activeEvent = null;

  // Fermer le menu au clic en dehors
  document.addEventListener('click', function(e) {
    if (!eventMenu.contains(e.target) && !e.target.closest('.fc-event')) {
      eventMenu.style.display = 'none';
    }
  });

  const calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth',
    locale: 'fr',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth'
    },
    eventClick: function(info) {
      activeEvent = info.event;
      const rect = info.el.getBoundingClientRect();
      eventMenu.style.display = 'block';
      eventMenu.style.top = (rect.bottom + window.scrollY + 5) + 'px';
      eventMenu.style.left = rect.left + 'px';
    },
    events: function(info, successCallback, failureCallback) {
      fetch('/api/entretiens/' + info.start.getFullYear() + '/' + (info.start.getMonth() + 1))
        .then(function(response) {
          if (!response.ok) {
            throw new Error('Erreur lors du chargement des entretiens');
          }
          return response.json();
        })
        .then(function(data) {
          const entretiensData = data.entretiens || data;
          const events = [];

          Object.entries(entretiensData).forEach(function([dateStr, entretiensJour]) {
            if (!entretiensJour || entretiensJour.length === 0) return;

            // Filtrer les entretiens non annulés
            const entretiensNonAnnules = entretiensJour.filter(function(e) {
              return e.etat !== 2;
            });

            if (entretiensNonAnnules.length === 0) return;

            // Tri par heure de début
            entretiensNonAnnules.sort(function(a, b) {
              return new Date(a.dateDebut) - new Date(b.dateDebut);
            });

            const premier = new Date(entretiensNonAnnules[0].dateDebut);
            const dernier = new Date(entretiensNonAnnules[entretiensNonAnnules.length - 1].dateDebut);

            const heureDebut = premier.toLocaleTimeString('fr-FR', {hour:'2-digit', minute:'2-digit'});
            const heureFin = dernier.toLocaleTimeString('fr-FR', {hour:'2-digit', minute:'2-digit'});

            // Déterminer l'état global de la journée
            const tousTermines = entretiensNonAnnules.every(function(e) {
              return e.etat === 1;
            });

            events.push({
              title: 'Entretiens de ' + heureDebut + ' à ' + heureFin,
              start: dateStr,
              allDay: true,
              className: tousTermines ? 'entretien-termine' : 'entretien-propose',
              textColor: '#fff',
              extendedProps: {
                entretiens: entretiensNonAnnules,
                tooltipContent: entretiensNonAnnules.map(function(e) {
                  const d1 = new Date(e.dateDebut);
                  const d2 = e.dateFin ? new Date(e.dateFin) : null;
                  const t1 = d1.toLocaleTimeString('fr-FR', {hour:'2-digit', minute:'2-digit'});
                  const t2 = d2 ? d2.toLocaleTimeString('fr-FR', {hour:'2-digit', minute:'2-digit'}) : '';
                  const slot = t2 ? t1 + ' à ' + t2 : t1;
                  const etatStr = e.etat === 1 ? '✓ Terminé' : '⏳ Proposé';
                  return slot + ' — ' + e.nom + ' ' + e.prenom + ' — ' + e.poste + ' (' + etatStr + ')';
                }).join('\n')
              }
            });
          });

          successCallback(events);
        })
        .catch(function(error) {
          console.error('Erreur:', error);
          failureCallback(error);
        });
    },
    eventDidMount: function(info) {
      const tooltipContent = info.event.extendedProps.tooltipContent || '';
      info.el.setAttribute('title', tooltipContent);
    }
  });

  // Gestionnaire pour le bouton d'annulation
  cancelButton.addEventListener('click', function() {
    if (!activeEvent) return;

    const entretien = activeEvent.extendedProps.entretiens[0];
    if (!entretien || !entretien.id) {
      alert('Impossible de trouver l\'entretien à annuler');
      return;
    }

    if (!confirm('Êtes-vous sûr de vouloir annuler cet entretien ?')) {
      return;
    }

    fetch('/api/entretiens/' + entretien.id + '/annuler', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    .then(function(response) {
      if (!response.ok) {
        throw new Error('Erreur lors de l\'annulation');
      }
      calendar.refetchEvents();
      eventMenu.style.display = 'none';
      activeEvent = null;
    })
    .catch(function(error) {
      console.error('Erreur:', error);
      alert('Erreur lors de l\'annulation de l\'entretien');
    });
  });

  calendar.render();
});