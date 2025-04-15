describe('Widget App', () => {
    const testWidget = {
        name: 'Test Widget',
        description: 'This is a test widget for Cypress.',
        price: 123.45,
    }

    beforeEach(() => {

        cy.visit('http://localhost:3000')
    })

    // Helper function to create a widget via API
    const createTestWidget = () => {
        cy.request({
            method: 'POST',
            url: 'http://localhost:9000/v1/widgets',
            body: testWidget,
        }).then((response) => {
            expect(response.status).to.equal(201);
            expect(response.body).to.deep.include(testWidget);
        });
    };

    it('loads the widget list', () => {
        cy.contains('List of widgets:')
    })

    it('creates a new widget', () => {
        cy.contains('Create Widget').click()

        cy.get('input[name="name"]').type(testWidget.name)
        cy.get('textarea[name="description"]').type(testWidget.description)
        cy.get('input[name="price"]').clear().type(testWidget.price.toString())

        cy.contains('Create Widget').click()

        cy.on('window:alert', (text) => {
            expect(text).to.equal('Widget created!')
        })
        createTestWidget();
    })

    it('find all widgets', () => {
        cy.contains('List of widgets:')
        cy.contains(testWidget.name)
        cy.contains(testWidget.description)
        cy.contains(`$${testWidget.price}`)
    })

    it('finds the widget by name', () => {
        cy.contains('Find by Name').click()
        cy.get('input[type="text"]').type(testWidget.name)
        cy.contains('Search').click()
        cy.wait(3000)
        cy.contains(testWidget.name)
        cy.contains(testWidget.description)
        cy.contains(`$${testWidget.price}`)
    })

    it('updates a widget', () => {
        const newDescription = 'Updated description from Cypress.'
        const newPrice = 543.21

        cy.contains('Find by Name').click()
        cy.get('input[type="text"]').type(testWidget.name)
        cy.contains('Search').click()
        cy.contains('Edit Widget').click()

        cy.get('textarea[name="description"]')
            .clear()
            .type(newDescription)
        cy.get('input[name="price"]')
            .clear()
            .type(newPrice.toString())

        cy.contains('Update Widget').click()

    })

    it('deletes the widget', () => {

        cy.contains('Find by Name').click()
        cy.get('input[type="text"]').clear().type(testWidget.name)
        cy.contains('Search').click()
        cy.contains('Delete Widget').click()
        cy.on('window:alert', (text) => {
            expect(text).to.equal('Widget deleted!')
        })
    })
})
